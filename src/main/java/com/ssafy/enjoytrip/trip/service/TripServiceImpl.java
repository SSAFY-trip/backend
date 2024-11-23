package com.ssafy.enjoytrip.trip.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoytrip.trip.adaptor.TripAdaptor;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.dto.TripUpdateDto;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.global.util.S3Util;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {
    private final TripAdaptor tripAdaptor;
    private final S3Util s3Util;

    public static final String tripImageBasePath = "trip/";

    @Value("${aws.s3.bucket-trip-base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public void createTrip(TripCreateDto tripDto) {
        tripDto.createUidAndImgUrlWith(baseUrl);
        tripAdaptor.insertTrip(tripDto.toEntity());
        s3Util.uploadFile(tripDto.getImage(), tripImageBasePath + tripDto.getUid());
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
    @Transactional
    public void updateTrip(Integer id, TripUpdateDto tripDto) {
        tripDto.setUpdateId(id);
        tripAdaptor.updateTrip(tripDto.toEntity());
    }

    @Override
    @Transactional
    public void deleteTrip(Integer id) {
        Trip trip = tripAdaptor.getTripById(id);
        s3Util.deleteFile(tripImageBasePath + trip.getUid());
        tripAdaptor.deleteTrip(id);
    }
}
