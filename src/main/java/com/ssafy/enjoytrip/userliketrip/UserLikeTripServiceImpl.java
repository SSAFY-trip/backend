package com.ssafy.enjoytrip.userliketrip;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLikeTripServiceImpl implements UserLikeTripService{
    private final UserLikeTripAdaptor userLikeTripAdaptor;
    public void updateLikeTrip(Long userId, Long tripId) {
        boolean isLiked = userLikeTripAdaptor.isLiked(userId, tripId);

        userLikeTripAdaptor.toggleLike(userId, tripId, !isLiked);
    }
    public List<TripResponseDto> getLikedTripsByUser(Long userId) {
        return userLikeTripAdaptor.findLikedTripsByUser(userId);
    }
    public List<UserResponseDTO> getUsersWhoLikedTrip(Long tripId) {
        return userLikeTripAdaptor.findUsersWhoLikedTrip(tripId);
    }
}

