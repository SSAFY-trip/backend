package com.ssafy.enjoytrip.controller;

import com.ssafy.enjoytrip.dto.EventCreateDto;
import com.ssafy.enjoytrip.dto.EventResponseDto;
import com.ssafy.enjoytrip.dto.EventUpdateMemoDto;
import com.ssafy.enjoytrip.dto.EventUpdateOrderDto;
import com.ssafy.enjoytrip.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trips/{tripId}/events")
@Validated
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<Void> createEvent(@PathVariable Integer tripId, @RequestBody @Valid EventCreateDto eventDto) {
        eventService.createEvent(tripId, eventDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Map<LocalDate, List<EventResponseDto>>> getOrderedEventsByTripId(@PathVariable Integer tripId) {
        Map<LocalDate, List<EventResponseDto>> events = eventService.getOrderedEventsByTripId(tripId);
        return ResponseEntity.ok(events);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Void> updateEventMemo(@PathVariable Integer eventId, @RequestBody @Valid EventUpdateMemoDto eventDto) {
        eventService.updateEventMemo(eventId, eventDto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/by-date/{date}")
    public ResponseEntity<Void> updateOrderOfEvents(@PathVariable Integer tripId, @PathVariable LocalDate date, @RequestBody @Valid List<EventUpdateOrderDto> eventDtos) {
        eventService.updateOrderOfEvents(tripId, date, eventDtos);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer tripId, @PathVariable Integer eventId) {
        eventService.deleteEvent(tripId, eventId);
        return ResponseEntity.noContent().build();
    }
}
