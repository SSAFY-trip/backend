package com.ssafy.enjoytrip.event.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.event.adaptor.EventAdaptor;
import com.ssafy.enjoytrip.openfeign.client.TMapClient;
import com.ssafy.enjoytrip.trip.adaptor.TripAdaptor;
import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.event.dto.EventCreateDto;
import com.ssafy.enjoytrip.event.dto.EventResponseDto;
import com.ssafy.enjoytrip.event.dto.EventUpdateMemoDto;
import com.ssafy.enjoytrip.event.dto.EventUpdateOrderDto;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventAdaptor eventAdaptor;

    @Mock
    private TripAdaptor tripAdaptor;

    @Mock
    private TMapClient tMapClient;

    @InjectMocks
    private EventServiceImpl eventService;

    private Trip trip;
    private Event event;

    @BeforeEach
    public void setup() {
        trip = Trip.builder()
                .id(1)
                .name("Test Trip")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();
    }

    @Test
    @DisplayName("Test Create Event - successful")
    void testCreateEvent() {
        // Given
        int tripId = 1;

        EventCreateDto eventDto = EventCreateDto.builder()
                .name("Incheon Internation Airport")
                .date(LocalDate.now())
                .memo("Test Event Memo")
                .latitude(12.12)
                .longitude(12.12)
                .category("교통")
                .build();

        event = eventDto.toEntity();

        // When
        eventService.createEvent(tripId, eventDto);

        // Then
        verify(eventAdaptor, times(1)).insertEvent(eq(tripId), refEq(event.getDate()), refEq(event));
    }

    @Test
    @DisplayName("Test Get Ordered Events By Trip ID - successful")
    void testGetOrderedEventsByTripId() {
        // Given
        int tripId = 1;

        // When
        Map<String, List<EventResponseDto>> events = eventService.getOrderedEventsByTripId(tripId);

        // Then
        verify(eventAdaptor, times(1)).getOrderedEventsByTripId(eq(tripId));
    }

    @Test
    @DisplayName("Test Update Event Memo - successful")
    void testUpdateEventMemo() {
        // Given
        int eventId = 1;
        EventUpdateMemoDto eventDto = EventUpdateMemoDto.builder().memo("updated memo").build();

        // When
        eventService.updateEventMemo(eventId, eventDto);

        // Then
        verify(eventAdaptor, times(1)).updateEventMemo(refEq(eventDto.toEntity()));
    }

    @Test
    @DisplayName("Test Update Order of Events - with valid data")
    void testUpdateOrderOfEvents() {
        // Given
        int tripId = trip.getId();
        LocalDate date = LocalDate.now();

        List<EventUpdateOrderDto> eventDtos = Arrays.asList(
                EventUpdateOrderDto.builder().id(1).order(1).build(),
                EventUpdateOrderDto.builder().id(2).order(3).build(),
                EventUpdateOrderDto.builder().id(3).order(2).build()
        );
        List<Event> events = eventDtos.stream().map(dto -> dto.toEntity()).collect(Collectors.toList());

        // When
        eventService.updateOrderOfEvents(tripId, date, eventDtos);

        // Then
        verify(eventAdaptor, times(1)).updateOrderOfEvents(eq(tripId), refEq(date), refEq(events));
    }

    @Test
    @DisplayName("Test Delete Event - successful")
    void testDeleteEvent() {
        // Given
        int eventId = 1;
        int tripId = 1;

        // When
        eventService.deleteEvent(tripId, eventId);

        // Then
        verify(eventAdaptor, times(1)).deleteEvent(tripId, eventId);
    }
}
