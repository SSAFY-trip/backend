package com.ssafy.enjoytrip.event.adaptor;

import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.event.exception.*;
import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.global.annotation.Adaptor;
import com.ssafy.enjoytrip.trip.adaptor.TripAdaptor;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.exception.DateNotWithinTripDurationException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Adaptor
@RequiredArgsConstructor
public class EventAdaptor {
    private final EventMapper eventMapper;
    private final TripAdaptor tripAdaptor;

    public void insertEvent(Integer tripId, LocalDate date, Event event) {
        validateTripIdAndDate(tripId, date);
        Event createdEvent = eventMapper.insertEvent(tripId, date, event);
        if (createdEvent.getOrder() == 20)
            throw new EventMaxCntException();
    }

    public Event getEventById(Integer id) {
        return Optional.of(eventMapper.getEventById(id))
                .orElseThrow(EventNotFoundException::new);
    }

    public Event getEventOfTripById(Integer tripId, Integer id) {
        validateEventId(id);
        return Optional.of(eventMapper.getEventOfTripById(tripId, id))
                .orElseThrow(EventOfTripNotFoundException::new);
    }

    public Map<String, LocalDate> getEventDateRangeOfTripId(Integer tripId) {
        validateTripId(tripId);
        return eventMapper.getEventDateRangeOfTripId(tripId);
    }

    public List<Integer> getEventIdsOfTripIdAndDate(Integer tripId, LocalDate date) {
        validateTripIdAndDate(tripId, date);
        return eventMapper.getEventIdsOfTripIdAndDate(tripId, date);
    }

    public List<Event> getEventsOfTripIdAndDate(Integer tripId, LocalDate date) {
        validateTripIdAndDate(tripId, date);
        return eventMapper.getEventsOfTripIdAndDate(tripId, date);
    }

    public List<String> getPlaceIdsOfTripId(Integer tripId) {
        validateTripId(tripId);
        return eventMapper.getPlaceIdsOfTripId(tripId);
    }

    public List<Event> getOrderedEventsByTripId(Integer tripId) {
        validateTripId(tripId);
        return eventMapper.getOrderedEventsByTripId(tripId);
    }

    public void updateEventMemo(Event event) {
        validateEventId(event.getId());
        eventMapper.updateEventMemo(event);
    }

    public void updateEventOrder(Event event) {
        validateEventId(event.getId());
        eventMapper.updateEventOrder(event);
    }

    public void updateOrderOfEvents(Integer tripId, LocalDate date, List<Event> events) {
        validateTripIdAndDate(tripId, date);
        Set<Integer> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
        Set<Integer> existingEventIds = eventMapper.getEventIdsOfTripIdAndDate(tripId, date).stream().collect(Collectors.toSet());

        int eventIdCnt = eventIds.size();
        eventIds.removeAll(existingEventIds);
        if (eventIds.size() > 0)
            throw new EventOfTripAndDateNotFoundException();
        else if (eventIdCnt != existingEventIds.size())
            throw new EventRequiredNotProvidedException();

        eventMapper.updateOrderOfEvents(tripId, date, events);
    }

    public void deleteEvent(Integer tripId, Integer id) {
        validateTripId(tripId);
        validateEventId(id);
        Optional.of(eventMapper.deleteEventOfTripById(tripId, id))
                .orElseThrow(EventOfTripNotFoundException::new);
    }

    public void deleteEvent(Integer id){
        validateEventId(id);
        eventMapper.deleteEventById(id);
    }

    public void deleteAllEvents() {
        eventMapper.deleteAllEvents();
    }

    public void resetAutoIncrement(){
        eventMapper.resetAutoIncrement();
    }

    /*
     * validation utils
     */
    public void validateEventId(Integer id){
        this.getEventById(id);
    }

    public void validateTripId(Integer tripId) {
        tripAdaptor.getTripById(tripId);
    }

    public void validateTripIdAndDate(Integer tripId, LocalDate date) {
        Trip trip = tripAdaptor.getTripById(tripId);
        if (date.isAfter(trip.getEndDate()) || date.isBefore(trip.getStartDate()))
            throw new DateNotWithinTripDurationException();
    }
}
