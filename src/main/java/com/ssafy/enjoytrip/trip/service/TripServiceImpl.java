package com.ssafy.enjoytrip.trip.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.enjoytrip.global.exception.exception.ResourceNotFoundException;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.dto.TripUpdateDto;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;

@Service
public class TripServiceImpl implements TripService {
    @Autowired
    private TripMapper tripMapper;

    @Override
    public void createTrip(TripCreateDto tripDto) {
        tripMapper.insertTrip(tripDto.toEntity());
    }

    @Override
    public TripResponseDto getTripById(Integer id) {
        Trip trip = tripMapper.getTripById(id);
        if (trip == null) {
            throw new ResourceNotFoundException("Trip not found with id = " + id);
        }
        return new TripResponseDto(trip);
    }

    @Override
    public List<TripResponseDto> getAllTrips() {
        return tripMapper.getAllTrips()
                .stream()
                .map(TripResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTrip(Integer id, TripUpdateDto tripDto) {
        tripDto.setUpdateId(id);
        int rowsAffected = tripMapper.updateTrip(tripDto.toEntity());
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Trip not found with id = " + tripDto.getId());
        }
    }

    @Override
    public void deleteTrip(Integer id) {
        int rowsAffected = tripMapper.deleteTrip(id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Trip not found with id = " + id);
        }
    }
}
