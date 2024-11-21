package com.ssafy.enjoytrip.userliketrip.service;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.userliketrip.adaptor.UserLikeTripAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserLikeTripServiceTest {
    @InjectMocks
    private UserLikeTripServiceImpl userLikeTripService;
    @Mock
    private UserLikeTripAdaptor userLikeTripAdaptor;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("updateLikeTrip - Toggle Like")
    void testUpdateLikeTrip() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;

        // Mocking
        when(userLikeTripAdaptor.isLiked(userId, tripId)).thenReturn(false);

        // When
        userLikeTripService.updateLikeTrip(userId, tripId);

        // Then
        verify(userLikeTripAdaptor, times(1)).isLiked(userId, tripId);
        verify(userLikeTripAdaptor, times(1)).toggleLike(userId, tripId, true);
    }
    @Test
    @DisplayName("updateLikeTrip - 기존 Like가 존재할 때 deleteLike 호출")
    void testUpdateLikeTripWithExistingLike() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;

        // Mocking: 좋아요 상태가 true라고 가정
        when(userLikeTripAdaptor.isLiked(userId, tripId)).thenReturn(true);

        // When
        userLikeTripService.updateLikeTrip(userId, tripId);

        // Then
        verify(userLikeTripAdaptor, times(1)).isLiked(userId, tripId);
        verify(userLikeTripAdaptor, times(1)).toggleLike(userId, tripId, false);
    }

    @Test
    @DisplayName("getLikedTripsByUser - Return Liked Trips")
    void testGetLikedTripsByUser() {
        // Given
        Long userId = 1L;
        TripResponseDto trip1 = TripResponseDto.builder().id(1).name("Trip 1").build();
        TripResponseDto trip2 = TripResponseDto.builder().id(2).name("Trip 2").build();
        List<TripResponseDto> likedTrips = Arrays.asList(trip1, trip2);

        // Mocking
        when(userLikeTripAdaptor.findLikedTripsByUser(userId)).thenReturn(likedTrips);

        // When
        List<TripResponseDto> result = userLikeTripService.getLikedTripsByUser(userId);

        // Then
        verify(userLikeTripAdaptor, times(1)).findLikedTripsByUser(userId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Trip 1");
    }

    @Test
    @DisplayName("getUsersWhoLikedTrip - Return Users Who Liked Trip")
    void testGetUsersWhoLikedTrip() {
        // Given
        Long tripId = 2L;
        UserResponseDTO user1 = UserResponseDTO.builder().id(1L).username("user1").build();
        UserResponseDTO user2 = UserResponseDTO.builder().id(2L).username("user2").build();
        List<UserResponseDTO> users = Arrays.asList(user1, user2);

        // Mocking
        when(userLikeTripAdaptor.findUsersWhoLikedTrip(tripId)).thenReturn(users);

        // When
        List<UserResponseDTO> result = userLikeTripService.getUsersWhoLikedTrip(tripId);

        // Then
        verify(userLikeTripAdaptor, times(1)).findUsersWhoLikedTrip(tripId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("user1");
    }
}