package com.ssafy.enjoytrip.tripmember.service;

import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.tripmember.adaptor.TripMemberAdaptor;
import com.ssafy.enjoytrip.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripMemberService {
    private final TripMemberAdaptor tripMemberAdaptor;
    public List<User> getUsersByTripId(Long tripId) {
        return tripMemberAdaptor.getTripMembers(tripId);
    }

    public List<TripResponseDto> getTripsByUserId(Long userId) {
        List<Trip> trips = tripMemberAdaptor.getTripsByUserId(userId);

        return trips.stream()
                .map(TripResponseDto::of)
                .collect(Collectors.toList());
    }

    public void addMemberToTrip(Long userId, Long tripId) {
        tripMemberAdaptor.save(userId, tripId);
    }

    public void removeMemberFromTrip(Long userId, Long tripId) {
        tripMemberAdaptor.remove(userId, tripId);
    }
}
