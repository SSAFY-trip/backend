package com.ssafy.enjoytrip.userliketrip.service;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.userliketrip.adaptor.UserLikeTripAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLikeTripServiceImpl implements UserLikeTripService{
    private final UserLikeTripAdaptor userLikeTripAdaptor;
    public void updateLikeTrip(Long userId, Long tripId) {
        userLikeTripAdaptor.toggleLike(userId, tripId);
    }
    public List<TripResponseDto> getLikedTripsByUser(Long userId) {
        return userLikeTripAdaptor.findLikedTripsByUser(userId);
    }
    public List<UserResponseDTO> getUsersWhoLikedTrip(Long tripId) {
        return userLikeTripAdaptor.findUsersWhoLikedTrip(tripId);
    }
}

