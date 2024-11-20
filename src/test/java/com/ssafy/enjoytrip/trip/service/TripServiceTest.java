package com.ssafy.enjoytrip.trip.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.trip.adaptor.TripAdaptor;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.dto.TripResponseDto;
import com.ssafy.enjoytrip.trip.dto.TripUpdateDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {
    @Mock
    private TripAdaptor tripAdaptor;

    @InjectMocks
    private TripServiceImpl tripService;

    @Test
    @DisplayName("Create Trip - call insertTrip()")
    void testCreateTrip_shouldCallInsertTrip() {
        // Given
        TripCreateDto tripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();
        Trip trip = tripCreateDto.toEntity();

        // When
        tripService.createTrip(tripCreateDto);

        // Then
        verify(tripAdaptor, times(1)).insertTrip(refEq(trip));
    }

    @Test
    @DisplayName("Get Trip - call getTripById()")
    void testGetTripById_shouldReturnTripResponseDto() {
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
        when(tripAdaptor.getTripById(1)).thenReturn(trip);

        // When
        TripResponseDto result = tripService.getTripById(1);

        // Then
        assertNotNull(result, "TripResponseDto should not be null");
        assertEquals(trip.getName(), result.getName(), "Trip name should match");
        verify(tripAdaptor, times(1)).getTripById(1);
    }

    @Test
    @DisplayName("Get All Trips - return TripResponseDto list, call getAllTrips()")
    void testGetAllTrips_shouldReturnListOfTripResponseDto() {
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
                .name("Rome")
                .startDate(LocalDate.of(2023, 2, 1))
                .endDate(LocalDate.of(2023, 2, 10))
                .tripOverview("An amazing trip to Rome.")
                .imgUrl("http://example.com/image2.jpg")
                .isPublic(true)
                .build();

        when(tripAdaptor.getAllTrips()).thenReturn(Arrays.asList(trip1, trip2));

        // When
        List<TripResponseDto> result = tripService.getAllTrips();

        // Then
        assertEquals(2, result.size(), "Should return a list with 2 trips");
        assertEquals("Paris", result.get(0).getName(), "First trip name should match");
        assertEquals("Rome", result.get(1).getName(), "Second trip name should match");
        verify(tripAdaptor, times(1)).getAllTrips();
    }

    @Test
    @DisplayName("Update Trip - calls updateTrip() with new fields")
    void testUpdateTrip_shouldUpdateTrip() {
        // Given
        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name("Updated Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("An updated trip overview.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(false)
                .build();
        tripUpdateDto.setUpdateId(1);

        Trip updatedTrip = tripUpdateDto.toEntity();

        // When
        tripService.updateTrip(1, tripUpdateDto);

        // Then
        verify(tripAdaptor, times(1)).updateTrip(refEq(updatedTrip));
    }

    @Test
    @DisplayName("Delete Trip - calls deleteById()")
    void deleteTrip_shouldDeleteTrip() {
        // When
        tripService.deleteTrip(1);

        // Then
        verify(tripAdaptor, times(1)).deleteTrip(1);
    }
}