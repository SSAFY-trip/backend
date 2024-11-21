package com.ssafy.enjoytrip.userliketrip;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;

import java.util.List;

public interface UserLikeTripService {
    public void updateLikeTrip(Long userId, Long tripId);
    public List<TripResponseDto> getLikedTripsByUser(Long userId);
    public List<UserResponseDTO> getUsersWhoLikedTrip(Long tripId);
}
