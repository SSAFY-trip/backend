package com.ssafy.enjoytrip.event.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoytrip.event.dto.*;
import com.ssafy.enjoytrip.event.service.EventService;


@RestController
@RequestMapping("/trips/{tripId}/events")
@Validated
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createEvent(@PathVariable Integer tripId,
                                            @RequestBody @Valid EventCreateDto eventDto) {
        eventService.createEvent(tripId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/by-date/{date}/optimize")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RouteOptimizationResponseDto> optimizeRouteOfEvents(@PathVariable LocalDate date,
                                                                              @PathVariable Integer tripId,
                                                                              @RequestBody RouteOptimizationRequestDto requestDto) {
        RouteOptimizationResponseDto responseDto = eventService.optimizeRouteOfEvents(tripId, date, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, List<EventResponseDto>>> getOrderedEventsByTripId(@PathVariable Integer tripId) {
        Map<String, List<EventResponseDto>> events = eventService.getOrderedEventsByTripId(tripId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, PlaceDetailResponseDto>> getPlaceDetailsOfAllEvents(@PathVariable Integer tripId) {
        Map<String, PlaceDetailResponseDto> eventDetails = eventService.getPlaceDetailsOfAllEvents(tripId);
        return ResponseEntity.ok(eventDetails);
    }

    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<PlaceDetailResponseDto>> getPlaceDetailsOfSearch(@RequestBody @Valid EventUpdateOrderDto.SearchRequestDto requestDto) {
        List<PlaceDetailResponseDto> places = eventService.getPlaceDetailsOfSearch(requestDto);
        return ResponseEntity.ok(places);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateEventMemo(@PathVariable Integer eventId,
                                                @RequestBody @Valid EventUpdateMemoDto eventDto) {
        eventService.updateEventMemo(eventId, eventDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/by-date/{date}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateOrderOfEvents(@PathVariable Integer tripId,
                                                    @PathVariable LocalDate date,
                                                    @RequestBody @Valid List<EventUpdateOrderDto> eventDtos) {
        eventService.updateOrderOfEvents(tripId, date, eventDtos);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer tripId,
                                            @PathVariable Integer eventId) {
        eventService.deleteEvent(tripId, eventId);
        return ResponseEntity.noContent().build();
    }
}
