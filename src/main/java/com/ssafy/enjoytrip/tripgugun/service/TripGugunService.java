package com.ssafy.enjoytrip.tripgugun.service;

import com.ssafy.enjoytrip.tripgugun.adaptor.TripGugunAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripGugunService {

    private final TripGugunAdaptor tripGugunAdaptor;

    public void addTripGugun(Integer tripId, Integer gugunId) {
        tripGugunAdaptor.addTripGugun(tripId, gugunId);
    }

    public void removeTripGugun(Integer tripId, Integer gugunId) {
        tripGugunAdaptor.removeTripGugun(tripId, gugunId);
    }

    public List<Integer> getGugunsByTripId(Integer tripId) {
        return tripGugunAdaptor.getGugunsByTripId(tripId);
    }

    public List<Integer> getTripsByGugunId(Integer gugunId) {
        return tripGugunAdaptor.getTripsByGugunId(gugunId);
    }
}
