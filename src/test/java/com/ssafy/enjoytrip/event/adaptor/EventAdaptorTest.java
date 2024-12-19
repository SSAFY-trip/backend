package com.ssafy.enjoytrip.event.adaptor;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.event.exception.*;
import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.trip.adaptor.TripAdaptor;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.exception.DateNotWithinTripDurationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventAdaptorTest {
    @Mock
    private TripAdaptor tripAdaptor;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventAdaptor eventAdaptor;
    
    private Trip trip;
    private Event event1, event2, event3;
    
    @BeforeEach
    void setup(){
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
        int eventId = 1;
        int eventOrder = 5;
        LocalDate date = trip.getStartDate();

        event1 = Event.builder()
                .name("Incheon Internation Airport")
                .memo("Test Event Memo")
                .latitude(12.12)
                .longitude(12.12)
                .category("교통")
                .build();

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);
        when(eventMapper.insertEvent(tripId, date, event1))
                .thenAnswer(invocation -> {
                    // set id using reflection
                    Event event = invocation.getArgument(2);

                    Field idField = Event.class.getDeclaredField("id");
                    Field orderField = Event.class.getDeclaredField("order");
                    idField.setAccessible(true);
                    idField.set(event, eventId);
                    orderField.setAccessible(true);
                    orderField.set(event, eventOrder);

                    return 1;
                });
        when(eventMapper.getEventById(eventId))
                .thenAnswer(invocation -> {
                    // set id using reflection
                    Integer id = invocation.getArgument(0);

                    Field idField = Event.class.getDeclaredField("id");
                    Field orderField = Event.class.getDeclaredField("order");
                    idField.setAccessible(true);
                    idField.set(event1, eventId);
                    orderField.setAccessible(true);
                    orderField.set(event1, eventOrder);

                    return event1;
                });

        // When
        eventAdaptor.insertEvent(tripId, date, event1);

        // Then
        verify(eventMapper, times(1)).insertEvent(eq(tripId), refEq(date), refEq(event1));
        verify(eventMapper, times(1)).getEventById(eq(eventId));
        verify(tripAdaptor, times(1)).getTripById(tripId);
    }

    @Test
    @DisplayName("Test Create Event - with invalid date")
    void testCreateEventWithInvalidDate() {
        // Given
        int tripId = 1;
        LocalDate date = trip.getEndDate().plusDays(1);

        event1 = Event.builder()
                .name("Incheon Internation Airport")
                .memo("Test Event Memo")
                .latitude(12.12)
                .longitude(12.12)
                .category("교통")
                .build();

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);

        // When & Then
        assertThrows(DateNotWithinTripDurationException.class, () -> eventAdaptor.insertEvent(tripId, date, event1));
        verify(eventMapper, never()).insertEvent(any(), any(LocalDate.class), any(Event.class));
    }

    @Test
    @DisplayName("Test Create Event - EventMaxCntException")
    void testCreateEventThrowsEventMaxCntException() {
        // Given
        int tripId = 1;
        int eventId = 1;
        int eventOrder = 20;
        LocalDate date = trip.getStartDate();

        event1 = Event.builder()
                .name("Incheon Internation Airport")
                .memo("Test Event Memo")
                .latitude(12.12)
                .longitude(12.12)
                .category("교통")
                .build();

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);
        when(eventMapper.insertEvent(tripId, date, event1))
                .thenAnswer(invocation -> {
                    // set id using reflection
                    Event event = invocation.getArgument(2);

                    Field idField = Event.class.getDeclaredField("id");
                    Field orderField = Event.class.getDeclaredField("order");
                    idField.setAccessible(true);
                    idField.set(event, eventId);
                    orderField.setAccessible(true);
                    orderField.set(event, eventOrder);

                    return 1;
                });
        when(eventMapper.getEventById(eventId))
                .thenAnswer(invocation -> {
                    // set id using reflection
                    Integer id = invocation.getArgument(0);

                    Field idField = Event.class.getDeclaredField("id");
                    Field orderField = Event.class.getDeclaredField("order");
                    idField.setAccessible(true);
                    idField.set(event1, eventId);
                    orderField.setAccessible(true);
                    orderField.set(event1, eventOrder);

                    return event1;
                });

        // When
        assertThrows(EventMaxCntException.class, () -> eventAdaptor.insertEvent(tripId, date, event1));

        // Then
        verify(eventMapper, times(1)).insertEvent(eq(tripId), refEq(date), refEq(event1));
        verify(eventMapper, times(1)).getEventById(eq(eventId));
        verify(tripAdaptor, times(1)).getTripById(tripId);
    }

    @Test
    @DisplayName("Test Get Event By Id - with valid id")
    void testGetEventById() {
        // Given
        int eventId = 1;
        event1 = Event.builder().id(eventId).build();

        when(eventMapper.getEventById(eventId)).thenReturn(event1);

        // When
        eventAdaptor.getEventById(eventId);

        // Then
        verify(eventMapper, times(1)).getEventById(eventId);
    }

    @Test
    @DisplayName("Test Get Event By Id - with invalid id")
    void testGetEventByIdThrowsEventNotFoundException () {
        // Given
        int eventId = 1;

        when(eventMapper.getEventById(eventId)).thenReturn(null);

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventAdaptor.getEventById(eventId));
        verify(eventMapper, times(1)).getEventById(eventId);
    }

    @Test
    @DisplayName("Test Get Event Of Trip By Id - successful")
    void testGetEventOfTripById() {
        // Given
        int tripId = 1;
        int eventId = 1;
        event1 = Event.builder().id(eventId).build();

        when(eventMapper.getEventById(eventId)).thenReturn(event1);
        when(eventMapper.getEventOfTripById(tripId, eventId)).thenReturn(event1);

        // When
        eventAdaptor.getEventOfTripById(tripId, eventId);

        // Then
        verify(eventMapper, times(1)).getEventById(eventId);
    }

    @Test
    @DisplayName("Test Get Event Of Trip By Id - with invalid event id")
    void testGetEventOfTripByIdInvalidEventId() {
        // Given
        int tripId = 1;
        int invalidEventId = 100;

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventAdaptor.getEventOfTripById(tripId, invalidEventId));
        verify(eventMapper, times(1)).getEventById(invalidEventId);
        verify(eventMapper, never()).getEventOfTripById(tripId, invalidEventId);
    }

    @Test
    @DisplayName("Test Get Event Of Trip By Id - with invalid trip id")
    void testGetEventOfTripByIdInvalidTripId() {
        // Given
        int invalidTripId = 1;
        int eventId = 100;
        event1 = Event.builder().id(eventId).build();

        when(eventMapper.getEventById(eventId)).thenReturn(event1);

        // When & Then
        assertThrows(EventOfTripNotFoundException.class, () -> eventAdaptor.getEventOfTripById(invalidTripId, eventId));
        verify(eventMapper, times(1)).getEventById(eventId);
        verify(eventMapper, times(1)).getEventOfTripById(invalidTripId, eventId);
    }

    @Test
    @DisplayName("Test Get Event Date Range Of Trip Id - successful")
    void testGetEventDateRangeOfTripId() {
        // Given
        int tripId = 1;

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);

        // When
        eventAdaptor.getEventDateRangeOfTripId(tripId);

        // Then
        verify(tripAdaptor, times(1)).getTripById(tripId);
        verify(eventMapper, times(1)).getEventDateRangeOfTripId(tripId);
    }

    @Test
    @DisplayName("Test Get Event Ids Of Trip And Date - successful")
    void testGetEventIdsOfTripAndDate() {
        // Given
        int tripId = 1;
        LocalDate date = trip.getEndDate();

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);

        // When
        eventAdaptor.getEventIdsOfTripIdAndDate(tripId, date);
        // Then
        verify(eventMapper, times(1)).getEventIdsOfTripIdAndDate(any(), any(LocalDate.class));
    }

    @Test
    @DisplayName("Test Get Event Ids Of Trip And Date - with invalid date")
    void testGetEventIdsOfTripAndDateWithInvalidDate() {
        // Given
        int tripId = 1;
        LocalDate date = trip.getEndDate().plusDays(1);

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);

        // When & Then
        assertThrows(DateNotWithinTripDurationException.class, () -> eventAdaptor.getEventIdsOfTripIdAndDate(tripId, date));
        verify(eventMapper, never()).getEventIdsOfTripIdAndDate(any(), any(LocalDate.class));
    }

    @Test
    @DisplayName("Test Update Order of Events - successful")
    void testUpdateOrderOfEvents() {
        // Given
        int tripId = trip.getId();
        LocalDate date = trip.getStartDate();

        List<Event> events = Arrays.asList(
                Event.builder().id(1).order(1).build(),
                Event.builder().id(2).order(3).build(),
                Event.builder().id(3).order(2).build()
        );
        List<Integer> existingEventIds = new ArrayList<>(Arrays.asList(1, 2, 3));

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);
        when(eventMapper.updateOrderOfEvents(eq(tripId), refEq(date), refEq(events))).thenReturn(3);
        when(eventMapper.getEventIdsOfTripIdAndDate(eq(tripId), refEq(date))).thenReturn(existingEventIds);

        // When
        eventAdaptor.updateOrderOfEvents(tripId, date, events);

        // Then
        verify(eventMapper, times(1)).updateOrderOfEvents(eq(tripId), refEq(date), refEq(events));
        verify(eventMapper, times(1)).getEventIdsOfTripIdAndDate(eq(tripId), refEq(date));
    }

    @Test
    @DisplayName("Test Update Order of Events - with event ids of different trip")
    void testUpdateOrderOfEventsThrowsEventOfTripAndDateNotFoundException() {
        // Given
        int tripId = trip.getId();
        LocalDate date = trip.getStartDate();

        List<Event> events = Arrays.asList(
                Event.builder().id(1).order(1).build(),
                Event.builder().id(2).order(3).build(),
                Event.builder().id(3).order(2).build()
        );
        List<Integer> existingEventIds = new ArrayList<>(Arrays.asList(1, 4, 5));

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);
        when(eventMapper.getEventIdsOfTripIdAndDate(eq(tripId), refEq(date))).thenReturn(existingEventIds);

        // When & Then
        assertThrows(EventOfTripAndDateNotFoundException.class, () -> eventAdaptor.updateOrderOfEvents(tripId, date, events));
        verify(eventMapper, never()).updateOrderOfEvents(eq(tripId), refEq(date), refEq(events));
        verify(eventMapper, times(1)).getEventIdsOfTripIdAndDate(eq(tripId), refEq(date));
    }

    @Test
    @DisplayName("Test Update Order of Events - with not all event ids provided")
    void testUpdateOrderOfEventsThrowsEventRequiredNotProvidedException() {
        // Given
        int tripId = trip.getId();
        LocalDate date = trip.getStartDate();

        List<Event> events = Arrays.asList(
                Event.builder().id(1).order(1).build(),
                Event.builder().id(2).order(3).build(),
                Event.builder().id(3).order(2).build()
        );
        List<Integer> existingEventIds = new ArrayList<>(Arrays.asList(1, 2, 3, 4));

        when(tripAdaptor.getTripById(tripId)).thenReturn(trip);
        when(eventMapper.getEventIdsOfTripIdAndDate(eq(tripId), refEq(date))).thenReturn(existingEventIds);

        // When & Then
        assertThrows(EventRequiredNotProvidedException.class, () -> eventAdaptor.updateOrderOfEvents(tripId, date, events));
        verify(eventMapper, never()).updateOrderOfEvents(eq(tripId), refEq(date), refEq(events));
        verify(eventMapper, times(1)).getEventIdsOfTripIdAndDate(eq(tripId), refEq(date));
    }

    @Test
    @DisplayName("Test Delete Event - with valid data")
    void testDeleteEvent() {
        // Given
        int eventId = 1;
        event1 = Event.builder().id(eventId).build();

        when(eventMapper.getEventById(eventId)).thenReturn(event1);
        when(eventMapper.deleteEventById(eventId)).thenReturn(1);

        // When
        eventAdaptor.deleteEvent(eventId);

        // Then
        verify(eventMapper, times(1)).deleteEventById(eventId);
    }

    @Test
    @DisplayName("Test Delete Event - with invalid event id")
    void testDeleteEventWithInvalidEventId() {
        // Given
        int eventId = 1;

        // When & Then
        assertThrows(EventNotFoundException.class, () -> eventAdaptor.deleteEvent(eventId));
        verify(eventMapper, never()).deleteEventById(eventId);
    }
}
