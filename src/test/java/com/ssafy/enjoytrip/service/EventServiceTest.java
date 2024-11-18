package com.ssafy.enjoytrip.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.ssafy.enjoytrip.exception.DataMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.domain.Event;
import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.dto.EventCreateDto;
import com.ssafy.enjoytrip.dto.EventResponseDto;
import com.ssafy.enjoytrip.dto.EventUpdateMemoDto;
import com.ssafy.enjoytrip.dto.EventUpdateOrderDto;
import com.ssafy.enjoytrip.exception.ResourceNotFoundException;
import com.ssafy.enjoytrip.repository.EventMapper;
import com.ssafy.enjoytrip.repository.TripMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {
    @Mock
    private EventMapper eventMapper;

    @Mock
    private TripMapper tripMapper;

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
    @DisplayName("Test Create Event - with valid data")
    void testCreateEvent() {
        // Given
        int tripId = 1;

        EventCreateDto eventDto = EventCreateDto.builder()
                .name("Incheon Internation Airport")
                .date(LocalDate.now())
                .memo("Test Event Memo")
                .latitude(12.12f)
                .longitude(12.12f)
                .category("교통")
                .build();
        event = eventDto.toEntity();
        when(tripMapper.getTripById(tripId)).thenReturn(trip);

        // When
        eventService.createEvent(tripId, eventDto);

        // Then
        verify(eventMapper, times(1)).insertEvent(eq(tripId), refEq(event.getDate()), refEq(event));
    }

    @Test
    @DisplayName("Test Create Event - with invalid date")
    void testCreateEventWithInvalidDate() {
        // Given
        int tripId = 1;

        EventCreateDto eventDto = EventCreateDto.builder()
                .name("Incheon Internation Airport")
                .date(LocalDate.now().minusDays(1))
                .memo("Test Event Memo")
                .latitude(12.12f)
                .longitude(12.12f)
                .category("교통")
                .build();
        event = eventDto.toEntity();
        when(tripMapper.getTripById(tripId)).thenReturn(trip);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(tripId, eventDto));

        assertEquals("Event date is outside of the trip's date range", exception.getMessage());
        verify(eventMapper, never()).insertEvent(eq(tripId), refEq(event.getDate()), refEq(event));
    }

    @Test
    @DisplayName("Test Create Event - with invalid trip id")
    void testCreateEventWithInvalidTripId() {
        // Given
        int tripId = 1;

        EventCreateDto eventDto = EventCreateDto.builder().build();
        when(tripMapper.getTripById(tripId)).thenReturn(null);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> eventService.createEvent(tripId, eventDto));

        assertEquals("Trip not found with id = " + tripId, exception.getMessage());
        verify(eventMapper, never()).insertEvent(any(), any(LocalDate.class), any(Event.class));
    }

    @Test
    @DisplayName("Test Get Ordered Events By Trip ID - with valid data")
    void testGetOrderedEventsByTripId() {
        // Given
        int tripId = 1;
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfterTomorrow = today.plusDays(2);

        when(tripMapper.getTripById(tripId)).thenReturn(trip);
        when(eventMapper.getOrderedEventsByTripId(tripId))
                .thenReturn(new ArrayList<>(Arrays.asList(
                        Event.builder().name("trip1").tripId(tripId).date(LocalDate.now()).order(1).build(),
                        Event.builder().name("trip2").tripId(tripId).date(LocalDate.now()).order(2).build(),
                        Event.builder().name("trip3").tripId(tripId).date(LocalDate.now()).order(3).build(),
                        Event.builder().name("trip4").tripId(tripId).date(LocalDate.now().plusDays(1)).order(1).build(),
                        Event.builder().name("trip5").tripId(tripId).date(LocalDate.now().plusDays(2)).order(1).build(),
                        Event.builder().name("trip6").tripId(tripId).date(LocalDate.now().plusDays(2)).order(2).build()
                )));

        // When
        Map<LocalDate, List<EventResponseDto>> events = eventService.getOrderedEventsByTripId(tripId);

        // Then
        verify(eventMapper, times(1)).getOrderedEventsByTripId(eq(tripId));

        // Verify there are 3 distinct dates, keys are in order, and have correct dates
        assertEquals(3, events.keySet().size());
        assertEquals(Arrays.asList(today, tomorrow, dayAfterTomorrow), new ArrayList<>(events.keySet()));

        // Verify each date has the correct number of events
        assertEquals(3, events.get(today).size());
        assertEquals(1, events.get(tomorrow).size());
        assertEquals(2, events.get(dayAfterTomorrow).size());

        // Verify the names and order of events under each date
        List<EventResponseDto> todayEvents = events.get(today);
        assertEquals("trip1", todayEvents.get(0).getName());
        assertEquals("trip2", todayEvents.get(1).getName());
        assertEquals("trip3", todayEvents.get(2).getName());

        List<EventResponseDto> tomorrowEvents = events.get(tomorrow);
        assertEquals("trip4", tomorrowEvents.get(0).getName());

        List<EventResponseDto> dayAfterTomorrowEvents = events.get(dayAfterTomorrow);
        assertEquals("trip5", dayAfterTomorrowEvents.get(0).getName());
        assertEquals("trip6", dayAfterTomorrowEvents.get(1).getName());
    }

    @Test
    @DisplayName("Test Get Ordered Events By Trip ID - with invalid trip id")
    void testGetOrderedEventsByTripIdWithInvalidTripId() {
        // Given
        int tripId = 1;

        when(tripMapper.getTripById(tripId)).thenReturn(null);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> eventService.getOrderedEventsByTripId(tripId));

        assertEquals("Trip not found with id = " + tripId, exception.getMessage());
    }

    @Test
    @DisplayName("Test Update Event Memo - with valid data")
    void testUpdateEventMemo() {
        // Given
        int eventId = 1;
        EventUpdateMemoDto eventDto = EventUpdateMemoDto.builder().memo("updated memo").build();

        when(eventMapper.updateEventMemo(any(Event.class))).thenReturn(1);

        // When
        eventService.updateEventMemo(eventId, eventDto);

        // Then
        verify(eventMapper, times(1)).updateEventMemo(any(Event.class));
    }

    @Test
    @DisplayName("Test Create Event - with invalid id")
    void testUpdateEventMemoWIthInvalidId() {
        // Given
        int eventId = 1;
        EventUpdateMemoDto eventDto = EventUpdateMemoDto.builder().memo("updated memo").build();

        when(eventMapper.updateEventMemo(any(Event.class))).thenReturn(0);

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> eventService.updateEventMemo(eventId, eventDto));

        assertEquals("Event not found with id = " + eventId, exception.getMessage());
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
        List<Integer> eventIds = new ArrayList<>(Arrays.asList(1, 2, 3));

        when(eventMapper.updateOrderOfEvents(eq(tripId), refEq(date), refEq(events))).thenReturn(3);
        when(eventMapper.getEventIdsOfTripIdAndDate(eq(tripId), refEq(date))).thenReturn(eventIds);

        // When
        eventService.updateOrderOfEvents(tripId, date, eventDtos);

        // Then
        verify(eventMapper, times(1)).updateOrderOfEvents(eq(tripId), refEq(date), refEq(events));
        verify(eventMapper, times(1)).getEventIdsOfTripIdAndDate(eq(tripId), refEq(date));
    }

    @Test
    @DisplayName("Test Update Order of Events - with not full event ids")
    void testUpdateOrderOfEventsWithNotFullEventIds() {
        // Given
        int tripId = trip.getId();
        LocalDate date = LocalDate.now();

        List<EventUpdateOrderDto> eventDtos = Arrays.asList(
                EventUpdateOrderDto.builder().id(1).order(1).build(),
                EventUpdateOrderDto.builder().id(2).order(3).build(),
                EventUpdateOrderDto.builder().id(3).order(2).build()
        );
        List<Event> events = eventDtos.stream().map(dto -> dto.toEntity()).collect(Collectors.toList());
        List<Integer> eventIds = new ArrayList<>(Arrays.asList(2, 3));

        when(eventMapper.getEventIdsOfTripIdAndDate(eq(tripId), refEq(date))).thenReturn(eventIds);

        // When & Then
        assertThrows(DataMismatchException.class, () -> eventService.updateOrderOfEvents(tripId, date, eventDtos));

        // Then
        verify(eventMapper, never()).updateOrderOfEvents(any(), any(), any());
        verify(eventMapper, times(1)).getEventIdsOfTripIdAndDate(eq(tripId), refEq(date));
    }

    @Test
    @DisplayName("Test Delete Event - with valid data")
    void testDeleteEvent() {
        // Given
        int eventId = 1;
        int tripId = 1;

        when(eventMapper.deleteEvent(tripId, eventId)).thenReturn(1);

        // When
        eventService.deleteEvent(tripId, eventId);

        // Then
        verify(eventMapper, times(1)).deleteEvent(tripId, eventId);
    }

    @Test
    @DisplayName("Test Delete Event - with invalid event id")
    void testDeleteEventWithInvalidEventId() {
        // Given
        int eventId = 1;
        int tripId = 1;

        when(eventMapper.deleteEvent(tripId, eventId)).thenReturn(0);

        // When & THen
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> eventService.deleteEvent(tripId, eventId));

        assertEquals("Event not found with id = " + eventId, exception.getMessage());
    }
}
