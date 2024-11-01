package com.ssafy.enjoytrip.service;

import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.repository.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {
    @Autowired
    private TripMapper tripMapper;

    public void createTrip(Trip trip) {
        tripMapper.insertTrip(trip);
    }

    public Trip getTripById(Integer id) {
        return tripMapper.getTripById(id);
    }

    public List<Trip> getAllTrips() {
        return tripMapper.getAllTrips();
    }

    public void updateTrip(Trip trip) {
        tripMapper.updateTrip(trip);
    }

    public void deleteTrip(Integer id) {
        tripMapper.deleteTrip(id);
    }
}