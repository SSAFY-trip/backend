package com.ssafy.enjoytrip.event.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.event.adaptor.EventAdaptor;
import com.ssafy.enjoytrip.event.dto.*;
import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.openfeign.client.KakaoClient;
import com.ssafy.enjoytrip.openfeign.client.TMapClient;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationRequestDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationResponseDto;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventAdaptor eventAdaptor;
    private final TMapClient tMapClient;
    private final KakaoClient kakaoClient;

    @Override
    public void createEvent(Integer tripId, EventCreateDto eventDto) {
        eventAdaptor.insertEvent(tripId, eventDto.getDate(), eventDto.toEntity());
    }

    @Override
    @Transactional
    public RouteOptimizationResponseDto optimizeRouteOfEvents(Integer tripId, LocalDate date, RouteOptimizationRequestDto requestDto) {
        List<Event> events = eventAdaptor.getEventsOfTripIdAndDate(tripId, date);
        List<Event> startEndEvents = requestDto.toEventList();

        TMapRouteOptimizationRequestDto tMapRequestDto = TMapRouteOptimizationRequestDto.toDto(events, startEndEvents);
        TMapRouteOptimizationResponseDto tMapResponseDto = tMapClient.getRouteOptimization20(tMapRequestDto);
        List<Event> optimizedEvents = tMapResponseDto.toEventList();

        eventAdaptor.updateOrderOfEvents(tripId, date, optimizedEvents);

        return RouteOptimizationResponseDto.of(optimizedEvents, tMapResponseDto);
    }

    @Override
    public Map<LocalDate, List<EventResponseDto>> getOrderedEventsByTripId(Integer tripId) {
        List<Event> events = eventAdaptor.getOrderedEventsByTripId(tripId);
        return events.stream()
                .map(EventResponseDto::of)
                .collect(Collectors.groupingBy(EventResponseDto::getDate, TreeMap::new, Collectors.toList()));
    }

    @Override
    public Map<String, PlaceDetailResponseDto> getPlaceDetailsOfAllEvents(Integer tripId) {
        List<String> placeIds = eventAdaptor.getPlaceIdsOfTripId(tripId);
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
        eventAdaptor.updateEventMemo(eventDto.toEntity());
    }

    @Override
    @Transactional
    public void updateOrderOfEvents(Integer tripId, LocalDate date, List<EventUpdateOrderDto> eventDtos) {
        List<Event> events = new ArrayList<>();

        for (int i = 0; i < eventDtos.size(); i++) {
            EventUpdateOrderDto eventDto = eventDtos.get(i);
            eventDto.setUpdateOrder(i + 1);
            events.add(eventDto.toEntity());
        }

        eventAdaptor.updateOrderOfEvents(tripId, date, events);
    }

    @Override
    public void deleteEvent(Integer tripId, Integer id) {
        eventAdaptor.deleteEvent(tripId, id);
    }

    private PlaceDetailResponseDto fetchPlaceDetail(String placeId) {
        PlaceDetailResponseDto eventDto = PlaceDetailResponseDto.of(tMapClient.getPlaceDetails(placeId));

        String query = eventDto.getName();
        List<String> imageUrls = kakaoClient.getImages(query).toImageUrlList();
        eventDto.setImageUrls(imageUrls);

        return eventDto;
    }
}
