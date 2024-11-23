package com.ssafy.enjoytrip.tripgugun.adaptor;

import com.ssafy.enjoytrip.tripgugun.exception.TripGugunAlreadyExistsException;
import com.ssafy.enjoytrip.tripgugun.mapper.TripGugunMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripGugunAdaptor {

    private final TripGugunMapper tripGugunMapper;

    public void addTripGugun(Integer tripId, Integer gugunId) {
        List<Integer> guguns = getGugunsByTripId(tripId);
        if (guguns.contains(gugunId)) {
            throw new TripGugunAlreadyExistsException();
        }
        tripGugunMapper.insertTripGugun(tripId, gugunId);
    }

    public void removeTripGugun(Integer tripId, Integer gugunId) {
        tripGugunMapper.deleteTripGugun(tripId, gugunId);
    }

    public List<Integer> getGugunsByTripId(Integer tripId) {
        return tripGugunMapper.findGugunsByTripId(tripId);
    }

    public List<Integer> getTripsByGugunId(Integer gugunId) {
        return tripGugunMapper.findTripsByGugunId(gugunId);
    }
}
