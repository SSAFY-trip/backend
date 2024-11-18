package com.ssafy.enjoytrip.trip.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ssafy.enjoytrip.service.TripService;
import com.ssafy.enjoytrip.dto.TripCreateDto;
import com.ssafy.enjoytrip.dto.TripResponseDto;
import com.ssafy.enjoytrip.dto.TripUpdateDto;

@RestController
@RequestMapping("/trips")
@Validated
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @PostMapping
    public ResponseEntity<Void> createTrip(@RequestBody @Valid TripCreateDto tripDto) {
        tripService.createTrip(tripDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDto> getTripById(@PathVariable Integer id) {
        TripResponseDto tripDto = tripService.getTripById(id);
        return ResponseEntity.ok(tripDto);
    }

    @GetMapping
    public ResponseEntity<List<TripResponseDto>> getAllTrips() {
        List<TripResponseDto> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateTrip(@PathVariable Integer id, @RequestBody @Valid TripUpdateDto tripDto) {
        tripService.updateTrip(id, tripDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Integer id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }
}
