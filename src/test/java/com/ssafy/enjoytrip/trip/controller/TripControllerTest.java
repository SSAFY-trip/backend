package com.ssafy.enjoytrip.trip.controller;

import java.time.LocalDate;
import java.util.Arrays;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssafy.enjoytrip.global.exception.GlobalExceptionHandler;
import com.ssafy.enjoytrip.global.exception.GlobalErrorCode;
import com.ssafy.enjoytrip.trip.exception.TripErrorCode;
import com.ssafy.enjoytrip.trip.exception.TripNotFoundException;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.dto.TripUpdateDto;
import com.ssafy.enjoytrip.trip.service.TripService;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import static com.ssafy.enjoytrip.TestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(TripController.class)
@WithMockUser
@ContextConfiguration(classes = {TripController.class, GlobalExceptionHandler.class})

class TripControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TripService tripService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Test Create Trip - with valid data, call createTrip()")
    void testCreateTrip() throws Exception {
        // Given
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "image content".getBytes());

        TripCreateDto tripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .image(mockImageFile)
                .isPublic(true)
                .build();


        // When
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/trips");
        ResultActions resultActions = mockMvc.perform(createMultiPartRequest(multipartRequest, tripCreateDto).with(csrf()));

        // Then
        resultActions.andExpect(status().isCreated());

        verify(tripService, times(1)).createTrip(any(TripCreateDto.class));
    }

    @Test
    @DisplayName("Test Create Trip - with invalid data, don't call createTrip()")
    void testCreateTripWithInvalidData() throws Exception {
        // Given
        TripCreateDto invalidTripCreateDto = TripCreateDto.builder()
                .name(RandomStringUtils.randomAlphanumeric(16))
                .tripOverview(RandomStringUtils.randomAlphanumeric(101))
                .isPublic(true)
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(post("/trips")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTripCreateDto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(
                        GlobalErrorCode.VALIDATION_FAILED.getCode(),
                        getCode(result),
                        "Expected error code does not match."))
                .andDo(result -> printResponse(result));

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
                .isPublic(true)
                .build();

        TripResponseDto tripResponseDto = TripResponseDto.of(trip);

        when(tripService.getTripById(1)).thenReturn(tripResponseDto);

        // When
        ResultActions resultActions = mockMvc.perform(get("/trips/1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Paris"));

        verify(tripService, times(1)).getTripById(1);
    }

    @Test
    @DisplayName("Test Get Trip By Id - with non-existing id")
    void testGetTripByNonExistingId() throws Exception {
        // Given
        int nonExistingId = 1;

        when(tripService.getTripById(nonExistingId))
                .thenThrow(new TripNotFoundException());

        // When
        ResultActions resultActions = mockMvc.perform(get("/trips/" + nonExistingId)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(
                        TripErrorCode.TRIP_NOT_FOUND.getCode(),
                        getCode(result),
                        "Expected error code does not match."));

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
                .isPublic(true)
                .build();

        Trip trip2 = Trip.builder()
                .id(2)
                .name("Paris2")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .isPublic(true)
                .build();

        TripResponseDto tripResponse1 = TripResponseDto.of(trip1);
        TripResponseDto tripResponse2 = TripResponseDto.of(trip2);

        when(tripService.getAllTrips()).thenReturn(Arrays.asList(tripResponse1, tripResponse2));

        // When
        ResultActions resultActions = mockMvc.perform(get("/trips")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));

        verify(tripService, times(1)).getAllTrips();
    }

    @Test
    @DisplayName("Test Update Trip - with valid data, existing id")
    void testUpdateTrip() throws Exception {
        // Given
        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name("Paris")
                .uid("uid")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 15))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("url")
                .isPublic(true)
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(patch("/trips/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tripUpdateDto)));

        // Then
        resultActions
                .andExpect(status().isNoContent());

        verify(tripService, times(1)).updateTrip(eq(1), any(TripUpdateDto.class));
    }

    @Test
    @DisplayName("Test Update Trip - with invalid data, existing id")
    void testUpdateTripWithInvalidData() throws Exception {
        // Given
        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name(RandomStringUtils.randomAlphanumeric(16))
                .tripOverview(RandomStringUtils.randomAlphanumeric(101))
                .isPublic(true)
                .build();
        tripUpdateDto.setUpdateId(1);

        // When
        ResultActions resultActions = mockMvc.perform(patch("/trips/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tripUpdateDto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(
                        GlobalErrorCode.VALIDATION_FAILED.getCode(),
                        getCode(result),
                        "Expected error code does not match."))
                .andDo(result -> printResponse(result));

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
                .isPublic(true)
                .build();

        tripUpdateDto.setUpdateId(nonExistingId);

        doThrow(new TripNotFoundException())
                .when(tripService).updateTrip(eq(nonExistingId), refEq(tripUpdateDto));

        // When
        ResultActions resultActions = mockMvc.perform(patch("/trips/" + nonExistingId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tripUpdateDto)));

        // Then
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(
                        TripErrorCode.TRIP_NOT_FOUND.getCode(),
                        getCode(result),
                        "Expected error code does not match."));
        ;
        verify(tripService, times(1)).updateTrip(eq(nonExistingId), refEq(tripUpdateDto));
    }

    @Test
    @DisplayName("Test Delete Trip - with existing id")
    void testDeleteTrip() throws Exception {
        // Given
        int tripId = 1;

        // When
        ResultActions resultActions = mockMvc.perform(delete("/trips/" + tripId)
                .with(csrf()));

        // Given
        resultActions.andExpect(status().isNoContent());
        verify(tripService, times(1)).deleteTrip(tripId);
    }

    @Test
    @DisplayName("Test Delete Trip - with non-existing id")
    void testDeleteTripWithNonExistingId() throws Exception {
        // Given
        int nonExistingId = 1;

        doThrow(new TripNotFoundException())
                .when(tripService).deleteTrip(nonExistingId);

        // When
        ResultActions resultActions = mockMvc.perform(delete("/trips/" + nonExistingId)
                .with(csrf()));

        // Given
        resultActions
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(
                        TripErrorCode.TRIP_NOT_FOUND.getCode(),
                        getCode(result),
                        "Expected error code does not match."));

        verify(tripService, times(1)).deleteTrip(nonExistingId);
    }
}