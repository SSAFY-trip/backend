package com.ssafy.enjoytrip.userliketrip;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-like-trip")
@RequiredArgsConstructor
public class UserLikeTripController {
    private final UserLikeTripService userLikeTripService;
    private final UserService userService;

    /**
     * 사용자가 좋아요 눌렀을때
     */
    @PostMapping("/{tripId}")
    public ResponseEntity<Void> updateLikeTrip(@PathVariable Long tripId) {
        Long userId = userService.getAuthenticatedUser().getId();

        userLikeTripService.updateLikeTrip(userId, tripId);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자가 좋아요한 여행 목록 조회.
     */
    @GetMapping("/user")
    public ResponseEntity<List<TripResponseDto>> getLikedTripsByUser() {
        Long userId = userService.getAuthenticatedUser().getId();
        List<TripResponseDto> likedTrips = userLikeTripService.getLikedTripsByUser(userId);
        return ResponseEntity.ok(likedTrips);
    }

    /**
     * 특정 여행을 좋아요한 사용자 목록 조회.
     */
    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<UserResponseDTO>> getUsersWhoLikedTrip(@PathVariable Long tripId) {
        List<UserResponseDTO> users = userLikeTripService.getUsersWhoLikedTrip(tripId);
        return ResponseEntity.ok(users);
    }
}
