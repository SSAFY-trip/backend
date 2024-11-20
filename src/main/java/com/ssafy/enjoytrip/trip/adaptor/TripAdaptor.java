package com.ssafy.enjoytrip.trip.adaptor;

import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.global.annotation.Adaptor;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.exception.TripCannotEndBeforeStartException;
import com.ssafy.enjoytrip.trip.exception.TripDateRangeConflictException;
import com.ssafy.enjoytrip.trip.exception.TripNotFoundException;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Adaptor
@RequiredArgsConstructor
public class TripAdaptor {
    private final TripMapper tripMapper;
    private final EventMapper eventMapper;

    public void insertTrip(Trip trip) {
        if(!trip.getStartDate().isBefore(trip.getEndDate()))
            throw new TripCannotEndBeforeStartException();
        tripMapper.insertTrip(trip);
    }

    public Trip getTripById(int id) {
        return Optional.ofNullable(tripMapper.getTripById(id))
                .orElseThrow(TripNotFoundException::new);
    }

    public List<Trip> getAllTrips() {
        return tripMapper.getAllTrips();
    }

    public void updateTrip(Trip trip) {
        Map<String, LocalDate> eventDateRange = eventMapper.getEventDateRangeOfTripId(trip.getId());
        if (!eventDateRange.isEmpty() &&
                (eventDateRange.get("minDate").isBefore(trip.getStartDate())
                        || eventDateRange.get("maxDate").isAfter(trip.getEndDate()))) {
            throw new TripDateRangeConflictException();
        }
        int rowsAffected = tripMapper.updateTrip(trip);
        if (rowsAffected == 0) {
            throw new TripNotFoundException();
        }
    }

    public void deleteTrip(int id) {
        int rowsAffected = tripMapper.deleteTrip(id);
        if (rowsAffected == 0) {
            throw new TripNotFoundException();
        }
    }

    public void deleteAllTrips() {
        tripMapper.deleteAllTrips();
    }

    public void resetAutoIncrement() {
        tripMapper.resetAutoIncrement();
    }
}
