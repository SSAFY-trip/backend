package com.ssafy.enjoytrip.tripmember.adaptor;

import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.tripmember.exception.TripMemberAlreadyExistsException;
import com.ssafy.enjoytrip.tripmember.exception.TripMemberNotFoundException;
import com.ssafy.enjoytrip.tripmember.mapper.TripMemberMapper;
import com.ssafy.enjoytrip.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripMemberAdaptor {
    private final TripMemberMapper tripMemberMapper;

    public List<User> getTripMembers(Long tripId) {
        List<User> members = tripMemberMapper.findUsersByTripId(tripId);
        if (members.isEmpty()) {
            throw new TripMemberNotFoundException();
        }
        return members;
    }

    public List<Trip> getTripsByUserId(Long userId) {
        return tripMemberMapper.findTripsByUserId(userId);
    }

    public void save(Long userId, Long tripId) {
        boolean exists = tripMemberMapper.existsByUserIdAndTripId(userId, tripId);
        if (exists) {
            throw new TripMemberAlreadyExistsException();
        }
        tripMemberMapper.save(userId, tripId);
    }

    public void remove(Long userId, Long tripId) {
        boolean exists = tripMemberMapper.existsByUserIdAndTripId(userId, tripId);
        if (!exists) {
            throw new TripMemberNotFoundException();
        }
        tripMemberMapper.remove(userId, tripId);
    }
}
