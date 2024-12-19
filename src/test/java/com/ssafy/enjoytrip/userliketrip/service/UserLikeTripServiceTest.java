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
    @DisplayName("updateLikeTrip - adaptor 호출 횟수 확인")
    void testUpdateLikeTrip() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;

        // When
        userLikeTripService.updateLikeTrip(userId, tripId);

        // Then
        verify(userLikeTripAdaptor, times(1)).toggleLike(userId, tripId);
    }
    @Test
    @DisplayName("getLikedTripsByUser - 사용자가 좋아요한 여행 목록 반환")
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
        assertThat(result.get(1).getName()).isEqualTo("Trip 2");
    }

    @Test
    @DisplayName("getUsersWhoLikedTrip - 특정 여행을 좋아요한 사용자 목록 반환")
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
        assertThat(result.get(1).getUsername()).isEqualTo("user2");
    }
}