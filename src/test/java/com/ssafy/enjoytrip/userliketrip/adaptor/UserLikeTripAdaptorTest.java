package com.ssafy.enjoytrip.userliketrip.adaptor;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.exception.TripNotFoundException;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.userliketrip.mapper.UserLikeTripMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserLikeTripAdaptorTest {
    @InjectMocks
    private UserLikeTripAdaptor userLikeTripAdaptor;
    @Mock
    private UserLikeTripMapper userLikeTripMapper;
    @Mock
    private TripMapper tripMapper;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("isLiked - 정상 동작 확인")
    void testIsLikedSuccess() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;

        // Mocking
        when(tripMapper.isTripValid(tripId)).thenReturn(true);
        when(userLikeTripMapper.isLiked(userId, tripId)).thenReturn(true);

        // When
        boolean result = userLikeTripAdaptor.isLiked(userId, tripId);

        // Then
        verify(tripMapper, times(1)).isTripValid(tripId);
        verify(userLikeTripMapper, times(1)).isLiked(userId, tripId);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("toggleLike - like가 true일 때 insertLike 호출")
    void testToggleLikeInsert() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;
        boolean like = true;

        // When
        userLikeTripAdaptor.toggleLike(userId, tripId, like);

        // Then
        verify(userLikeTripMapper, times(1)).insertLike(userId, tripId);
        verify(userLikeTripMapper, never()).deleteLike(anyLong(), anyLong());
    }

    @Test
    @DisplayName("toggleLike - like가 false일 때 deleteLike 호출")
    void testToggleLikeDelete() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;
        boolean like = false;

        // When
        userLikeTripAdaptor.toggleLike(userId, tripId, like);

        // Then
        verify(userLikeTripMapper, times(1)).deleteLike(userId, tripId);
        verify(userLikeTripMapper, never()).insertLike(anyLong(), anyLong());
    }
    @Test
    @DisplayName("isLiked - TripNotFoundException 호출")
    void testIsLikedTripNotFoundException() {
        // Given
        Long userId = 1L;
        Long tripId = 2L;

        // Mocking: tripMapper에서 해당 tripId가 유효하지 않음을 반환
        when(tripMapper.isTripValid(tripId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userLikeTripAdaptor.isLiked(userId, tripId))
                .isInstanceOf(TripNotFoundException.class);
    }

    @Test
    @DisplayName("findUsersWhoLikedTrip - TripNotFoundException 호출")
    void testFindUsersWhoLikedTripTripNotFoundException() {
        // Given
        Long tripId = 3L;

        // Mocking: tripMapper에서 해당 tripId가 유효하지 않음을 반환
        when(tripMapper.isTripValid(tripId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userLikeTripAdaptor.findUsersWhoLikedTrip(tripId))
                .isInstanceOf(TripNotFoundException.class);
    }
    @Test
    @DisplayName("findLikedTripsByUser - 정상 동작 확인")
    void testFindLikedTripsByUser() {
        // Given
        Long userId = 1L;
        TripResponseDto trip1 = TripResponseDto.builder()
                .id(1)
                .name("Trip 1")
                .build();
        TripResponseDto trip2 = TripResponseDto.builder()
                .id(2)
                .name("Trip 2")
                .build();
        List<TripResponseDto> trips = Arrays.asList(trip1, trip2);

        // Mocking
        when(userLikeTripMapper.findLikedTripsByUser(userId)).thenReturn(trips);

        // When
        List<TripResponseDto> result = userLikeTripAdaptor.findLikedTripsByUser(userId);

        // Then
        verify(userLikeTripMapper, times(1)).findLikedTripsByUser(userId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Trip 1");
    }
    @Test
    @DisplayName("findUsersWhoLikedTrip - 정상 동작 확인")
    void testFindUsersWhoLikedTrip() {
        // Given
        Long tripId = 2L;
        UserResponseDTO user1 = UserResponseDTO.builder()
                .id(1L)
                .username("user1")
                .build();
        UserResponseDTO user2 = UserResponseDTO.
                builder()
                .id(2L)
                .username("user2").build();
        List<UserResponseDTO> users = Arrays.asList(user1, user2);

        // Mocking
        when(tripMapper.isTripValid(tripId)).thenReturn(true);
        when(userLikeTripMapper.selectUsersWhoLikedTrip(tripId)).thenReturn(users);

        // When
        List<UserResponseDTO> result = userLikeTripAdaptor.findUsersWhoLikedTrip(tripId);

        // Then
        verify(tripMapper, times(1)).isTripValid(tripId);
        verify(userLikeTripMapper, times(1)).selectUsersWhoLikedTrip(tripId);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUsername()).isEqualTo("user1");
    }
}