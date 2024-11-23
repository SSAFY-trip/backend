package com.ssafy.enjoytrip.tripgugun.controller;

import com.ssafy.enjoytrip.tripgugun.service.TripGugunService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trip-gugun")
@RequiredArgsConstructor
public class TripGugunController {

    private final TripGugunService tripGugunService;

    @PostMapping
    public ResponseEntity<Void> addTripGugun(@RequestParam Integer tripId, @RequestParam Integer gugunId) {
        tripGugunService.addTripGugun(tripId, gugunId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeTripGugun(@RequestParam Integer tripId, @RequestParam Integer gugunId) {
        tripGugunService.removeTripGugun(tripId, gugunId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Integer>> getGugunsByTripId(@PathVariable Integer tripId) {
        return ResponseEntity.ok(tripGugunService.getGugunsByTripId(tripId));
    }

    @GetMapping("/gugun/{gugunId}")
    public ResponseEntity<List<Integer>> getTripsByGugunId(@PathVariable Integer gugunId) {
        return ResponseEntity.ok(tripGugunService.getTripsByGugunId(gugunId));
    }
}
