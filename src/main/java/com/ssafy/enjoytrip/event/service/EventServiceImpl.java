package com.ssafy.enjoytrip.event.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.ssafy.enjoytrip.exception.DataMismatchException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.domain.Event;
import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.dto.EventCreateDto;
import com.ssafy.enjoytrip.dto.EventResponseDto;
import com.ssafy.enjoytrip.dto.EventUpdateMemoDto;
import com.ssafy.enjoytrip.dto.EventUpdateOrderDto;
import com.ssafy.enjoytrip.exception.ResourceNotFoundException;
import com.ssafy.enjoytrip.repository.EventMapper;
import com.ssafy.enjoytrip.repository.TripMapper;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventMapper eventMapper;
    private final TripMapper tripMapper;

    @Override
    public void createEvent(Integer tripId, EventCreateDto eventDto) {
        Trip trip = Optional.ofNullable(tripMapper.getTripById(tripId))
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id = " + tripId));

        validateEventDateWithinTripRange(eventDto.getDate(), trip);

        eventMapper.insertEvent(tripId, eventDto.getDate(), eventDto.toEntity());
    }

    @Override
    public Map<LocalDate, List<EventResponseDto>> getOrderedEventsByTripId(Integer tripId) {
        Trip trip = tripMapper.getTripById(tripId);
        if (trip == null) {
            throw new ResourceNotFoundException("Trip not found with id = " + tripId);
        }
        List<Event> events = eventMapper.getOrderedEventsByTripId(tripId);
        return events.stream().map(EventResponseDto::new).collect(Collectors.groupingBy(EventResponseDto::getDate, TreeMap::new, Collectors.toList()));
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
        if(!isEqual){
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
}
