package com.ssafy.enjoytrip.event.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.event.dto.*;
import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.global.exception.exception.DataMismatchException;
import com.ssafy.enjoytrip.global.exception.exception.ExternalServiceException;
import com.ssafy.enjoytrip.global.exception.exception.ResourceNotFoundException;
import com.ssafy.enjoytrip.openfeign.KakaoClient;
import com.ssafy.enjoytrip.openfeign.TMapClient;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationRequestDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationResponseDto;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventMapper eventMapper;
    private final TripMapper tripMapper;
    private final TMapClient tMapClient;
    private final KakaoClient kakaoClient;

    @Override
    public void createEvent(Integer tripId, EventCreateDto eventDto) {
        Trip trip = Optional.ofNullable(tripMapper.getTripById(tripId))
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id = " + tripId));

        validateEventDateWithinTripRange(eventDto.getDate(), trip);

        eventMapper.insertEvent(tripId, eventDto.getDate(), eventDto.toEntity());
    }

    @Override
    @Transactional
    public RouteOptimizationResponseDto optimizeRouteOfEvents(Integer tripId, LocalDate date, RouteOptimizationRequestDto requestDto) {
        List<Event> events = eventMapper.getEventsOfTripIdAndDate(tripId, date);
        List<Event> startEndEvents = requestDto.toEventList();

        TMapRouteOptimizationRequestDto tMapRequestDto = TMapRouteOptimizationRequestDto.toDto(events, startEndEvents);
        TMapRouteOptimizationResponseDto tMapResponseDto = tMapClient.getRouteOptimization20(tMapRequestDto);
        List<Event> optimizedEvents = tMapResponseDto.toEventList();

        eventMapper.updateOrderOfEvents(tripId, date, optimizedEvents);

        return RouteOptimizationResponseDto.toDto(optimizedEvents, tMapResponseDto);
    }

    @Override
    public Map<LocalDate, List<EventResponseDto>> getOrderedEventsByTripId(Integer tripId) {
        List<Event> events = eventMapper.getOrderedEventsByTripId(tripId);
        return events.stream()
                .map(EventResponseDto::new)
                .collect(Collectors.groupingBy(EventResponseDto::getDate, TreeMap::new, Collectors.toList()));
    }

    @Override
    public Map<String, PlaceDetailResponseDto> getPlaceDetailsOfAllEvents(Integer tripId) {
        List<String> placeIds = eventMapper.getPlaceIdsOfTripId(tripId);
        Map<String, PlaceDetailResponseDto> eventDetails = new HashMap<>();
        for (String placeId : placeIds) {
            eventDetails.put(placeId, fetchPlaceDetail(placeId));
        }
        return eventDetails;
    }

    @Override
    public List<PlaceDetailResponseDto> getPlaceDetailsOfSearch(EventUpdateOrderDto.SearchRequestDto requestDto) {
        List<PlaceDetailResponseDto> places = new ArrayList<>();

        List<String> placeIds = tMapClient.getPlaceSearch(
                requestDto.getQuery(),
                requestDto.getAreaLLCode(),
                requestDto.getAreaLMCode(),
                requestDto.getRadius(),
                requestDto.getCenterLon(),
                requestDto.getCenterLat(),
                requestDto.getPage()
        ).toPlaceIdList();

        for (String placeId : placeIds) {
            places.add(fetchPlaceDetail(placeId));
        }

        return placeIds.stream()
                .map(this::fetchPlaceDetail)
                .collect(Collectors.toList());
    }

    @Override
    public void updateEventMemo(Integer id, EventUpdateMemoDto eventDto) {
        eventDto.setUpdateId(id);
        int rowsAffected = eventMapper.updateEventMemo(eventDto.toEntity());
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event not found with id = " + id);
        }
    }

    @Override
    @Transactional
    public void updateOrderOfEvents(Integer tripId, LocalDate date, List<EventUpdateOrderDto> eventDtos) {
        List<Event> events = new ArrayList<>();
        Set<Integer> eventIds = new HashSet<>();

        for (int i = 0; i < eventDtos.size(); i++) {
            EventUpdateOrderDto eventDto = eventDtos.get(i);

            eventDto.setUpdateOrder(i + 1);

            events.add(eventDto.toEntity());
            eventIds.add(eventDto.getId());
        }

        Set<Integer> existingEventIds = eventMapper.getEventIdsOfTripIdAndDate(tripId, date).stream().collect(Collectors.toSet());
        boolean isEqual = eventIds.equals(existingEventIds);
        if (!isEqual) {
            throw new DataMismatchException("The given event ids don't match existing events");
        }

        eventMapper.updateOrderOfEvents(tripId, date, events);
    }

    @Override
    public void deleteEvent(Integer tripId, Integer id) {
        int rowsAffected = eventMapper.deleteEvent(tripId, id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Event not found with id = " + id);
        }
    }

    private void validateEventDateWithinTripRange(LocalDate eventDate, Trip trip) {
        if (!(eventDate.isEqual(trip.getStartDate()) || eventDate.isAfter(trip.getStartDate())) || !(eventDate.isEqual(trip.getEndDate()) || eventDate.isBefore(trip.getEndDate()))) {
            throw new IllegalArgumentException("Event date is outside of the trip's date range");
        }
    }

    private PlaceDetailResponseDto fetchPlaceDetail(String placeId) {
        try {
            PlaceDetailResponseDto eventDto = new PlaceDetailResponseDto(tMapClient.getPlaceDetails(placeId));

            String query = eventDto.getName();
            List<String> imageUrls = kakaoClient.getImages(query).toImageUrlList();
            eventDto.setImageUrls(imageUrls);

            return eventDto;
        } catch (Exception e) {
            throw new ExternalServiceException("Failed to fetch event details for place ID: " + placeId);
        }
    }
}
