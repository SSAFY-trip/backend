package com.ssafy.enjoytrip.trip.service;

import java.util.List;

import com.ssafy.enjoytrip.dto.TripCreateDto;
import com.ssafy.enjoytrip.dto.TripResponseDto;
import com.ssafy.enjoytrip.dto.TripUpdateDto;

public interface TripService {
    void createTrip(TripCreateDto tripDto);

    TripResponseDto getTripById(Integer id);

    List<TripResponseDto> getAllTrips();

    void updateTrip(Integer id, TripUpdateDto tripDto);

    void deleteTrip(Integer id);
}
