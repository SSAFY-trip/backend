package com.ssafy.enjoytrip.userliketrip.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.global.exception.GlobalExceptionHandler;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.domain.Role;
import com.ssafy.enjoytrip.user.domain.User;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.user.mapper.UserMapper;
import com.ssafy.enjoytrip.user.service.UserService;
import com.ssafy.enjoytrip.userliketrip.service.UserLikeTripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ActiveProfiles("test")
@WebMvcTest(UserLikeTripController.class)
@WithMockUser
@ContextConfiguration(classes = {UserLikeTripController.class, GlobalExceptionHandler.class})
class UserLikeTripControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private UserLikeTripService userLikeTripService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @Test
    @DisplayName("Like Trip - Success")
    void testLikeTrip() throws Exception {
        // Given: Mock User와 Trip ID 설정
        Long mockUserId = 1L;
        Long tripId = 1L;

        // Mock User 객체 생성
        User mockUser = User.localUserBuilder()
                .username("mockUser")
                .password("mockPassword")
                .name("Mock User")
                .role(Role.ROLE_USER)
                .build();
        ReflectionTestUtils.setField(mockUser, "id", mockUserId); // Mock User ID 설정

        // UserService Mock 설정
        when(userService.getAuthenticatedUser()).thenReturn(mockUser);

        // When: API 호출
        mockMvc.perform(post("/user-like-trip/{tripId}", tripId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Then: 서비스 호출 검증
        verify(userLikeTripService, times(1)).updateLikeTrip(mockUserId, tripId);
    }

    @Test
    @DisplayName("Get Liked Trips By User - Success")
    void testGetLikedTripsByUser() throws Exception {
        // Given: Mock User와 반환 데이터 설정
        Long mockUserId = 1L;
        TripResponseDto trip1 = TripResponseDto.builder().id(1).name("Trip 1").build();
        TripResponseDto trip2 = TripResponseDto.builder().id(2).name("Trip 2").build();
        List<TripResponseDto> likedTrips = Arrays.asList(trip1, trip2);

        // Mock UserService에서 인증된 사용자 반환
        User mockUser = User.localUserBuilder()
                .username("mockUser")
                .password("mockPassword")
                .name("Mock User")
                .role(Role.ROLE_USER)
                .build();
        ReflectionTestUtils.setField(mockUser, "id", mockUserId); // Mock User ID 설정

        when(userService.getAuthenticatedUser()).thenReturn(mockUser); // MockUser 반환
        when(userLikeTripService.getLikedTripsByUser(mockUserId)).thenReturn(likedTrips); // likedTrips 반환

        // When: API 호출
        mockMvc.perform(get("/user-like-trip/user")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 상태 코드 검증
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // ContentType 검증
                .andExpect(jsonPath("$.length()").value(2)) // 배열 길이 검증
                .andExpect(jsonPath("$[0].name").value("Trip 1")) // 첫 번째 요소 이름 검증
                .andExpect(jsonPath("$[1].name").value("Trip 2")); // 두 번째 요소 이름 검증

        // Then: 서비스 호출 확인
        verify(userLikeTripService, times(1)).getLikedTripsByUser(mockUserId);
    }
    @Test
    @DisplayName("Get Users Who Liked Trip - Success")
    void testGetUsersWhoLikedTrip() throws Exception {
        // Given
        Long tripId = 1L;

        UserResponseDTO user1 = UserResponseDTO.builder().id(1L).username("user1").build();
        UserResponseDTO user2 = UserResponseDTO.builder().id(2L).username("user2").build();
        List<UserResponseDTO> users = Arrays.asList(user1, user2);

        when(userLikeTripService.getUsersWhoLikedTrip(tripId)).thenReturn(users);

        // When
        mockMvc.perform(get("/user-like-trip/trip/{tripId}", tripId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"));

        // Then
        verify(userLikeTripService, times(1)).getUsersWhoLikedTrip(tripId);
    }
}