package com.ssafy.enjoytrip.userliketrip.adaptor;

import com.ssafy.enjoytrip.global.annotation.Adaptor;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.exception.TripNotFoundException;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.userliketrip.mapper.UserLikeTripMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Adaptor
@RequiredArgsConstructor
public class UserLikeTripAdaptor {
    private final UserLikeTripMapper userLikeTripMapper;
    private final TripMapper tripMapper;
    public void toggleLike(Long userId, Long tripId) {
        validateTripExistence(tripId);

        if (userLikeTripMapper.isLiked(userId, tripId)) {
            userLikeTripMapper.deleteLike(userId, tripId);
        } else {
            userLikeTripMapper.insertLike(userId, tripId);
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
