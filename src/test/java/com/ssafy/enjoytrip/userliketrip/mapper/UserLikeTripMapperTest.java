package com.ssafy.enjoytrip.userliketrip.mapper;

import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
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
class UserLikeTripMapperTest {
    @Autowired
    private UserLikeTripMapper userLikeTripMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    void setUp() {
        // 기존 데이터 초기화
        jdbcTemplate.update("DELETE FROM user_like_trip");
        jdbcTemplate.update("DELETE FROM trip");
        jdbcTemplate.update("DELETE FROM user");

        // 필요한 데이터 삽입
        jdbcTemplate.update(
                "INSERT INTO user (id, username, name, email, password, role) VALUES (?, ?, ?, ?, ?, ?)",
                1L, "testUser", "Test Name", "test@example.com", "securePassword123", "ROLE_USER"
        );
        jdbcTemplate.update("INSERT INTO trip (id, name, start_date, end_date, trip_overview, img_url, is_public) VALUES (?, ?, ?, ?, ?, ?, ?)",
                1L, "Test Trip", "2023-01-01", "2023-01-10", "Overview", "url", true);
        jdbcTemplate.update("INSERT INTO user_like_trip (user_id, trip_id) VALUES (?, ?)", 1L, 1L);
    }
    @Test
    @DisplayName("insertLike - 좋아요 추가")
    void testInsertLike() {
        // Given
        Long userId = 1L;
        Long tripId = 1L;

        // When
        userLikeTripMapper.insertLike(userId, tripId);

        // Then
        Boolean isLiked = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) > 0 FROM user_like_trip WHERE user_id = ? AND trip_id = ?",
                Boolean.class,
                userId, tripId
        );
        assertThat(isLiked).isTrue();
    }

    @Test
    @DisplayName("deleteLike - 좋아요 제거")
    void testDeleteLike() {
        // Given
        Long userId = 1L;
        Long tripId = 1L;

        // When
        userLikeTripMapper.deleteLike(userId, tripId);

        // Then
        Boolean isLiked = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) > 0 FROM user_like_trip WHERE user_id = ? AND trip_id = ?",
                Boolean.class,
                userId, tripId
        );
        assertThat(isLiked).isFalse();
    }

    @Test
    @DisplayName("isLiked - 좋아요 여부 확인")
    void testIsLiked() {
        // Given
        Long userId = 1L;
        Long tripId = 1L;

        // When
        boolean liked = userLikeTripMapper.isLiked(userId, tripId);

        // Then
        assertThat(liked).isTrue();

        // Negative Case
        boolean notLiked = userLikeTripMapper.isLiked(999L, 999L);
        assertThat(notLiked).isFalse();
    }

    @Test
    @DisplayName("findLikedTripsByUser - 사용자가 좋아요한 여행 조회")
    void testFindLikedTripsByUser() {
        // Given
        Long userId = 1L;

        // When
        List<TripResponseDto> likedTrips = userLikeTripMapper.findLikedTripsByUser(userId);

        // Then
        assertThat(likedTrips).hasSize(1);
        TripResponseDto trip = likedTrips.get(0);
        assertThat(trip.getName()).isEqualTo("Test Trip");
        assertThat(trip.getImgUrl()).isEqualTo("url");
    }

    @Test
    @DisplayName("selectUsersWhoLikedTrip - 특정 여행을 좋아요한 사용자 조회")
    void testSelectUsersWhoLikedTrip() {
        // Given
        Long tripId = 1L;

        // When
        List<UserResponseDTO> users = userLikeTripMapper.selectUsersWhoLikedTrip(tripId);

        // Then
        assertThat(users).hasSize(1);
        UserResponseDTO user = users.get(0);
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getName()).isEqualTo("Test Name");
    }
}
