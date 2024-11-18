package com.ssafy.enjoytrip.mapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
public class EventMapperTest {
    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TripMapper tripMapper;

    private Event event1, event2, event3;

    private Trip trip;

    @BeforeEach
    public void setup() {
        trip = Trip.builder()
                .id(1)
                .name("Test Trip")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .build();

        tripMapper.insertTrip(trip);
    }

    @AfterEach
    void tearDown() {
        eventMapper.deleteAllEvents();
        eventMapper.resetAutoIncrement();
        tripMapper.deleteAllTrips();
        tripMapper.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test Insert Event")
    void testInsertEvent() {
        // GIven
        event1 = Event.builder()
                .name("Test Event 1")
                .date(LocalDate.now())
                .memo("Test Memo 1")
                .latitude(12.34f)
                .longitude(56.78f)
                .category("Category 1")
                .build();

        // When
        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);

        // Then
        Event fetchedEvent = eventMapper.getEventById(1);
        assertNotNull(fetchedEvent, "Fetched event should not be null");
        assertNotNull(fetchedEvent.getId(), "Trip ID should be generated");
        assertEquals(event1.getName(), fetchedEvent.getName(), "Event name should match");
        assertEquals(1, fetchedEvent.getOrder(), "Event order should be automatically generated");
    }

    @Test
    @DisplayName("Test Insert Event - auto-incremented id & order value")
    void testInsertEventAutoIncrementedId() {
        // GIven
        event1 = Event.builder()
                .name("Test Event 1")
                .date(LocalDate.now())
                .order(1)
                .memo("Test Memo 1")
                .latitude(12.34f)
                .longitude(56.78f)
                .category("Category 1")
                .build();

        // When
        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);
        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);

        // Then
        Event fetchedEvent = eventMapper.getEventById(event1.getId());
        assertEquals(fetchedEvent.getId(), 2, "Event ID should be auto-incremented");
        assertEquals(2, fetchedEvent.getOrder(), "Event order should be automatically generated");
    }

    @Test
    @DisplayName("Test Get Ordered Events By Trip ID")
    void testGetOrderedEventsByTripId() {
        // Given
        event1 = Event.builder()
                .name("Test Event 1")
                .date(LocalDate.now())
                .order(1)
                .build();
        event2 = Event.builder()
                .name("Test Event 2")
                .date(LocalDate.now().plusDays(1))
                .order(1)
                .build();
        event3 = Event.builder()
                .name("Test Event 3")
                .date(LocalDate.now())
                .order(2)
                .build();
        ;

        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);
        eventMapper.insertEvent(trip.getId(), event2.getDate(), event2);
        eventMapper.insertEvent(trip.getId(), event3.getDate(), event3);

        // When
        List<Event> events = eventMapper.getOrderedEventsByTripId(trip.getId());

        // Then
        assertTrue(events.get(0).getName().equals(event1.getName()));
        assertTrue(events.get(1).getName().equals(event3.getName()));
        assertTrue(events.get(2).getName().equals(event2.getName()));
    }

    @Test
    @DisplayName("Test Update Event Memo")
    void testUpdateEventMemo() {
        // Given
        event1 = Event.builder()
                .memo("Test Memo 1")
                .build();

        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);

        Event newEvent = Event.builder()
                .id(event1.getId())
                .memo("Updated Memo")
                .build();

        // When
        int rowsEffected = eventMapper.updateEventMemo(newEvent);

        // Then
        Event fetchedEvent = eventMapper.getEventById(event1.getId());
        assert (rowsEffected == 1);
        assertTrue(fetchedEvent.getMemo().equals(newEvent.getMemo()));
    }

    @Test
    @DisplayName("Test Update Event Order")
    void testUpdateEventOrder() {
        // Given
        event1 = Event.builder()
                .tripId(trip.getId())
                .build();

        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);

        Event newEvent = Event.builder()
                .id(event1.getId())
                .order(5)
                .tripId(trip.getId())
                .build();

        // When
        int rowsEffected = eventMapper.updateEventOrder(newEvent);

        // Then
        Event fetchedEvent = eventMapper.getEventById(event1.getId());
        assert (rowsEffected == 1);
        assertTrue(fetchedEvent.getOrder().equals(newEvent.getOrder()));
    }

    @Test
    @DisplayName("Test Update Event Order - with invalid trip id")
    void testUpdateEventOrderWithInvalidTripId() {
        // Given
        event1 = Event.builder()
                .order(1)
                .tripId(trip.getId())
                .build();

        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);

        Event newEvent = Event.builder()
                .id(event1.getId())
                .order(5)
                .tripId(trip.getId() + 1)
                .build();

        // When
        int rowsEffected = eventMapper.updateEventOrder(newEvent);

        // Then
        assert (rowsEffected == 0);
    }

    @Test
    @DisplayName("Test Update Order of Events")
    void testUpdateOrderOfEvents() {
        // Given
        int tripId = trip.getId();
        LocalDate date = LocalDate.now();
        event1 = Event.builder()
                .tripId(tripId)
                .date(date)
                .build();
        event2 = Event.builder()
                .tripId(tripId)
                .date(date)
                .build();

        eventMapper.insertEvent(tripId, date, event1);
        eventMapper.insertEvent(tripId, date, event2);

        Event newEvent1 =  Event.builder()
                .tripId(tripId)
                .date(date)
                .id(event1.getId())
                .order(2)
                .build();
        Event newEvent2 =  Event.builder()
                .tripId(tripId)
                .date(date)
                .id(event2.getId())
                .order(1)
                .build();

        List<Event> events = new ArrayList<>(Arrays.asList(newEvent1, newEvent2));

        // When
        int rowsEffected = eventMapper.updateOrderOfEvents(tripId, date, events);

        // Then
        assert (rowsEffected == 2);

        Event fetchedEvent = eventMapper.getEventById(event1.getId());
        assertEquals(2, fetchedEvent.getOrder());
    }

    @Test
    @DisplayName("Test Delete Event By Id")
    void testDeleteEventById() {
        // Given
        int tripId = 1;
        event1 = Event.builder().tripId(1).build();

        eventMapper.insertEvent(trip.getId(), event1.getDate(), event1);

        // When
        int rowsEffected = eventMapper.deleteEvent(tripId, event1.getId());

        // Then
        Event fetchedEvent = eventMapper.getEventById(event1.getId());
        assert (rowsEffected == 1);
        assertNull(fetchedEvent);
    }

}
