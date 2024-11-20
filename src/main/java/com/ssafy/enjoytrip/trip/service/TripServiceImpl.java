package com.ssafy.enjoytrip.trip.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.trip.adaptor.TripAdaptor;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.dto.TripUpdateDto;
import com.ssafy.enjoytrip.trip.domain.Trip;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripAdaptor tripAdaptor;

    @Override
    public void createTrip(TripCreateDto tripDto) {
        tripAdaptor.insertTrip(tripDto.toEntity());
    }

    @Override
    public TripResponseDto getTripById(Integer id) {
        Trip trip = tripAdaptor.getTripById(id);
        return TripResponseDto.of(trip);
    }

    @Override
    public List<TripResponseDto> getAllTrips() {
        return tripAdaptor.getAllTrips()
                .stream()
                .map(trip -> TripResponseDto.of(trip))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTrip(Integer id, TripUpdateDto tripDto) {
        tripDto.setUpdateId(id);
        tripAdaptor.updateTrip(tripDto.toEntity());
    }

    @Override
    public void deleteTrip(Integer id) {
        tripAdaptor.deleteTrip(id);
    }
}
