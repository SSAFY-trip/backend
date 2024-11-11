package com.ssafy.enjoytrip.controller;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.dto.TripCreateDto;
import com.ssafy.enjoytrip.dto.TripResponseDto;
import com.ssafy.enjoytrip.dto.TripUpdateDto;
import com.ssafy.enjoytrip.exception.GlobalExceptionHandler;
import com.ssafy.enjoytrip.exception.ResourceNotFoundException;
import com.ssafy.enjoytrip.repository.TripMapper;
import com.ssafy.enjoytrip.service.TripService;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(TripController.class)
@WithMockUser
@ContextConfiguration(classes = { TripController.class, GlobalExceptionHandler.class })
class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TripService tripService;

    @Mock
    private TripMapper tripMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Test Create Trip - with valid data, call createTrip()")
    void testCreateTrip() throws Exception {
        // Given
        TripCreateDto tripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        // When
        mockMvc.perform(post("/trips")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripCreateDto)))
                .andExpect(status().isCreated());

        // Then
        verify(tripService, times(1)).createTrip(any(TripCreateDto.class));
    }

    @Test
    @DisplayName("Test Create Trip - with invalid data, don't call createTrip()")
    void testCreateTripWithInvalidData() throws Exception {
        // Given
        TripCreateDto invalidTripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2022, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        // When
        mockMvc.perform(post("/trips")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTripCreateDto)))
                .andExpect(status().isBadRequest());

        // Then
        verify(tripService, never()).createTrip(any(TripCreateDto.class));
    }

    @Test
    @DisplayName("Test Get Trip By Id - with existing id")
    void testGetTripById() throws Exception {
        // Given
        Trip trip = Trip.builder()
                .id(1)
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        TripResponseDto tripResponseDto = new TripResponseDto(trip);

        when(tripService.getTripById(1)).thenReturn(tripResponseDto);

        // When
        mockMvc.perform(get("/trips/1")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Paris"));

        // Then
        verify(tripService, times(1)).getTripById(1);
    }

    @Test
    @DisplayName("Test Get Trip By Id - with non-existing id")
    void testGetTripByNonExistingId() throws Exception {
        // Given
        int nonExistingId = 1;

        when(tripService.getTripById(nonExistingId))
                .thenThrow(new ResourceNotFoundException("Trip not found with id = " + nonExistingId));

        // When
        mockMvc.perform(get("/trips/" + nonExistingId)
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Trip not found with id = " + nonExistingId));

        // Then
        verify(tripService, times(1)).getTripById(1);
    }

    @Test
    @DisplayName("Test Get All trips")
    void testGetAllTrips() throws Exception {
        // Given
        Trip trip1 = Trip.builder()
                .id(1)
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        Trip trip2 = Trip.builder()
                .id(2)
                .name("Paris2")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        TripResponseDto tripResponse1 = new TripResponseDto(trip1);
        TripResponseDto tripResponse2 = new TripResponseDto(trip2);

        when(tripService.getAllTrips()).thenReturn(Arrays.asList(tripResponse1, tripResponse2));

        // When
        mockMvc.perform(get("/trips")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        // Then
        verify(tripService, times(1)).getAllTrips();
    }

    @Test
    @DisplayName("Test Update Trip - with valid data, existing id")
    void testUpdateTrip() throws Exception {
        // Given
        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 15))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        // When
        mockMvc.perform(patch("/trips/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripUpdateDto)))
                .andExpect(status().isNoContent());

        // Then
        verify(tripService, times(1)).updateTrip(eq(1), any(TripUpdateDto.class));
    }

    @Test
    @DisplayName("Test Update Trip - with invalid data, existing id")
    void testUpdateTripWithInvalidData() throws Exception {
        // Given
        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2022, 1, 15))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();
        tripUpdateDto.setUpdateId(1);

        // When
        mockMvc.perform(patch("/trips/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripUpdateDto)))
                .andExpect(status().isBadRequest());

        // Then
        verify(tripService, never()).updateTrip(eq(1), any(TripUpdateDto.class));
    }

    @Test
    @DisplayName("Test Update Trip - with valid data, non-existing id")
    void testUpdateTripWithNonExistingId() throws Exception {
        // Given
        int nonExistingId = 1;

        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 15))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();

        tripUpdateDto.setUpdateId(nonExistingId);

        doThrow(new ResourceNotFoundException("Trip not found with id = " + nonExistingId))
                .when(tripService).updateTrip(eq(nonExistingId), refEq(tripUpdateDto));

        // When
        mockMvc.perform(patch("/trips/" + nonExistingId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripUpdateDto)))
                .andExpect(status().isNotFound());

        // Then
        verify(tripService, times(1)).updateTrip(eq(nonExistingId), refEq(tripUpdateDto));
    }

    @Test
    @DisplayName("Test Delete Trip - with existing id")
    void testDeleteTrip() throws Exception {
        // Given
        int tripId = 1;

        // When
        mockMvc.perform(delete("/trips/" + tripId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Given
        verify(tripService, times(1)).deleteTrip(tripId);
    }

    @Test
    @DisplayName("Test Delete Trip - with non-existing id")
    void testDeleteTripWithNonExistingId() throws Exception {
        // Given
        int nonExistingId = 1;

        doThrow(new ResourceNotFoundException("Trip not found with id = " + nonExistingId))
                .when(tripService).deleteTrip(nonExistingId);

        // When
        mockMvc.perform(delete("/trips/" + nonExistingId)
                        .with(csrf()))
                .andExpect(status().isNotFound());

        // Given
        verify(tripService, times(1)).deleteTrip(nonExistingId);
    }
}