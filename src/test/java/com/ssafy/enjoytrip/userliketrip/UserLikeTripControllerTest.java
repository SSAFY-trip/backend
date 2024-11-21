package com.ssafy.enjoytrip.userliketrip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.user.dto.UserResponseDTO;
import com.ssafy.enjoytrip.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserLikeTripControllerTest {

    @Mock
    private UserLikeTripService userLikeTripService;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserLikeTripController userLikeTripController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userLikeTripController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Like Trip - Success")
    @WithMockUser
    void testLikeTrip() throws Exception {
        Long tripId = 1L;
        Long userId = 2L;
        when(userService.getAuthenticatedUser().getId()).thenReturn(userId);

        mockMvc.perform(post("/user-like-trip/{tripId}", tripId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userLikeTripService, times(1)).updateLikeTrip(userId, tripId);
    }

    @Test
    @DisplayName("Get Liked Trips By User - Success")
    @WithMockUser
    void testGetLikedTripsByUser() throws Exception {
        Long userId = 2L;
        when(userService.getAuthenticatedUser().getId()).thenReturn(userId);

        TripResponseDto trip1 = TripResponseDto.builder().id(1).name("Trip 1").build();
        TripResponseDto trip2 = TripResponseDto.builder().id(2).name("Trip 2").build();
        List<TripResponseDto> likedTrips = Arrays.asList(trip1, trip2);
        when(userLikeTripService.getLikedTripsByUser(userId)).thenReturn(likedTrips);

        mockMvc.perform(get("/user-like-trip/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Trip 1"));

        verify(userLikeTripService, times(1)).getLikedTripsByUser(userId);
    }

    @Test
    @DisplayName("Get Users Who Liked Trip - Success")
    @WithMockUser
    void testGetUsersWhoLikedTrip() throws Exception {
        Long tripId = 1L;

        UserResponseDTO user1 = UserResponseDTO.builder().id(1L).username("user1").build();
        UserResponseDTO user2 = UserResponseDTO.builder().id(2L).username("user2").build();
        List<UserResponseDTO> users = Arrays.asList(user1, user2);
        when(userLikeTripService.getUsersWhoLikedTrip(tripId)).thenReturn(users);

        mockMvc.perform(get("/user-like-trip/trip/{tripId}", tripId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"));

        verify(userLikeTripService, times(1)).getUsersWhoLikedTrip(tripId);
    }
}
