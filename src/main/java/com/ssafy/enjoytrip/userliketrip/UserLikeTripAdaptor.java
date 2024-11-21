package com.ssafy.enjoytrip.userliketrip;

import com.ssafy.enjoytrip.global.annotation.Adaptor;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.exception.TripNotFoundException;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class UserLikeTripAdaptor {
    UserLikeTripMapper userLikeTripMapper;
    TripMapper tripMapper;
    public boolean isLiked(Long userId, Long tripId) {
        validateTripExistence(tripId);

        return userLikeTripMapper.isLiked(userId, tripId);
    }
    public void toggleLike(Long userId, Long tripId, boolean like) {
        if (like) {
            userLikeTripMapper.insertLike(userId, tripId);
        } else {
            userLikeTripMapper.deleteLike(userId, tripId);
        }
    }
    public List<TripResponseDto> findLikedTripsByUser(Long userId) {
        return userLikeTripMapper.findLikedTripsByUser(userId);
    }
    public List<UserResponseDTO> findUsersWhoLikedTrip(Long tripId) {
        validateTripExistence(tripId);

        return userLikeTripMapper.selectUsersWhoLikedTrip(tripId);
    }
    private void validateTripExistence(Long tripId){
        if(!tripMapper.isTripValid(tripId))
            throw new TripNotFoundException();
    }
}
