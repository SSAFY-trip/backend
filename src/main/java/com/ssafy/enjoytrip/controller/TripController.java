package com.ssafy.enjoytrip.controller;

import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping
    public void createTrip(@RequestBody Trip trip) {
        tripService.createTrip(trip);
    }

    @GetMapping("/{id}")
    public Trip getTripById(@PathVariable Integer id) {
        return tripService.getTripById(id);
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @PutMapping("/{id}")
    public void updateTrip(@PathVariable Integer id, @RequestBody Trip trip) {
        trip.setId(id);
        tripService.updateTrip(trip);
    }

    @DeleteMapping("/{id}")
    public void deleteTrip(@PathVariable Integer id) {
        tripService.deleteTrip(id);
    }
}