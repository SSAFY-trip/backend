package com.ssafy.enjoytrip.userliketrip.integration;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.userliketrip.mapper.UserLikeTripMapper;
import com.ssafy.enjoytrip.userliketrip.service.UserLikeTripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserLikeTripIntegrationTest {

    @Autowired
    private UserLikeTripMapper userLikeTripMapper;

    @Autowired
    private UserLikeTripService userLikeTripService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // 데이터 초기화
        jdbcTemplate.update("DELETE FROM user_like_trip");
        jdbcTemplate.update("DELETE FROM trip");
        jdbcTemplate.update("DELETE FROM user");

        // Trip 데이터 삽입
        jdbcTemplate.update("INSERT INTO trip (id, name, start_date, end_date, trip_overview, img_url, is_public) VALUES (?, ?, ?, ?, ?, ?, ?)",
                1L, "My First Trip", "2024-12-01", "2024-12-10", "A fun trip to enjoy the city.", "https://example.com/trip-image.jpg", true);
        jdbcTemplate.update("INSERT INTO trip (id, name, start_date, end_date, trip_overview, img_url, is_public) VALUES (?, ?, ?, ?, ?, ?, ?)",
                2L, "Second", "2024-11-25", "2024-11-30", "Exploring the mountains.", "https://example.com/mountain.jpg", false);
        jdbcTemplate.update("INSERT INTO trip (id, name, start_date, end_date, trip_overview, img_url, is_public) VALUES (?, ?, ?, ?, ?, ?, ?)",
                3L, "Beach G", "2025-01-15", "2025-01-20", "Relaxing at the beach.", null, true);

        // User 데이터 삽입
        jdbcTemplate.update("INSERT INTO user (id, username, name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)",
                1L, "user1", "User One", "user1@example.com", "password1", "ROLE_USER");
        jdbcTemplate.update("INSERT INTO user (id, username, name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)",
                2L, "user2", "User Two", "user2@example.com", "password2", "ROLE_USER");

        // UserLikeTrip 데이터 삽입
        jdbcTemplate.update("INSERT INTO user_like_trip (user_id, trip_id) VALUES (?, ?)", 1L, 1L);
        jdbcTemplate.update("INSERT INTO user_like_trip (user_id, trip_id) VALUES (?, ?)", 2L, 1L);
        jdbcTemplate.update("INSERT INTO user_like_trip (user_id, trip_id) VALUES (?, ?)", 2L, 3L);
    }

    @Test
    @DisplayName("사용자가 좋아요한 여행 조회")
    void testFindLikedTripsByUser() {
        // Given
        Long userId = 2L;

        // When
        List<TripResponseDto> likedTrips = userLikeTripService.getLikedTripsByUser(userId);

        // Then
        assertThat(likedTrips).hasSize(2);
        assertThat(likedTrips.get(0).getName()).isEqualTo("My First Trip");
        assertThat(likedTrips.get(1).getName()).isEqualTo("Beach G");
    }

    @Test
    @DisplayName("특정 여행을 좋아요한 사용자 조회")
    void testGetUsersWhoLikedTrip() {
        // Given
        Long tripId = 1L;

        // When
        List<UserResponseDTO> users = userLikeTripService.getUsersWhoLikedTrip(tripId);

        // Then
        assertThat(users).hasSize(2);
        assertThat(users.get(0).getUsername()).isEqualTo("user1");
        assertThat(users.get(1).getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("좋아요 상태 업데이트 및 확인")
    void testUpdateLikeTrip() {
        // Given
        Long userId = 1L;
        Long tripId = 3L;

        // Before: 좋아요 여부 확인
        boolean isLikedBefore = userLikeTripMapper.isLiked(userId, tripId);
        assertThat(isLikedBefore).isFalse();

        // When: 좋아요 추가
        userLikeTripService.updateLikeTrip(userId, tripId);

        // After: 좋아요 여부 확인
        boolean isLikedAfter = userLikeTripMapper.isLiked(userId, tripId);
        assertThat(isLikedAfter).isTrue();

        // When: 좋아요 제거
        userLikeTripService.updateLikeTrip(userId, tripId);

        // After: 좋아요 여부 확인
        boolean isLikedRemoved = userLikeTripMapper.isLiked(userId, tripId);
        assertThat(isLikedRemoved).isFalse();
    }
}
