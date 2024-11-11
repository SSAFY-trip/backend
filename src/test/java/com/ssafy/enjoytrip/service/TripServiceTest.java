package com.ssafy.enjoytrip.service;

import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.exception.ResourceNotFoundException;
import com.ssafy.enjoytrip.repository.TripMapper;
import com.ssafy.enjoytrip.dto.TripCreateDto;
import com.ssafy.enjoytrip.dto.TripResponseDto;
import com.ssafy.enjoytrip.dto.TripUpdateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripMapper tripMapper;

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
        verify(tripMapper, times(1)).insertTrip(refEq(trip));
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
        when(tripMapper.getTripById(1)).thenReturn(trip);

        // When
        TripResponseDto result = tripService.getTripById(1);

        // Then
        assertNotNull(result, "TripResponseDto should not be null");
        assertEquals(trip.getName(), result.getName(), "Trip name should match");
        verify(tripMapper, times(1)).getTripById(1);
    }

    @Test
    @DisplayName("Get Trip - raise ResourceNotFoundException when not found")
    void testGetTripById_shouldThrowExceptionWhenTripNotFound() {
        // Given
        when(tripMapper.getTripById(1)).thenReturn(null);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> tripService.getTripById(1));
        verify(tripMapper, times(1)).getTripById(1);
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

        when(tripMapper.getAllTrips()).thenReturn(Arrays.asList(trip1, trip2));

        // When
        List<TripResponseDto> result = tripService.getAllTrips();

        // Then
        assertEquals(2, result.size(), "Should return a list with 2 trips");
        assertEquals("Paris", result.get(0).getName(), "First trip name should match");
        assertEquals("Rome", result.get(1).getName(), "Second trip name should match");
        verify(tripMapper, times(1)).getAllTrips();
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
        when(tripMapper.updateTrip(refEq(updatedTrip))).thenReturn(1);

        // When
        tripService.updateTrip(1, tripUpdateDto);

        // Then
        verify(tripMapper, times(1)).updateTrip(refEq(updatedTrip));
    }

    @Test
    @DisplayName("Update Trip - raise ResourceNotFoundException when not found")
    void testUpdateTrip_shouldThrowExceptionWhenTripNotFound() {
        // Given
        TripUpdateDto tripUpdateDto = TripUpdateDto.builder()
                .name("Updated Trip")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("Updated overview")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(false)
                .build();
        tripUpdateDto.setUpdateId(1);

        Trip updatedTrip = tripUpdateDto.toEntity();
        when(tripMapper.updateTrip(refEq(updatedTrip))).thenReturn(0);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> tripService.updateTrip(1, tripUpdateDto));
        verify(tripMapper, times(1)).updateTrip(refEq(updatedTrip));
    }

    @Test
    @DisplayName("Delete Trip - calls deleteById()")
    void deleteTrip_shouldDeleteTrip() {
        // Given
        when(tripMapper.deleteTrip(1)).thenReturn(1);

        // When
        tripService.deleteTrip(1);

        // Then
        verify(tripMapper, times(1)).deleteTrip(1);
    }

    @Test
    @DisplayName("Delete Trip - raise ResourceNotFoundException when not found")
    void deleteTrip_shouldThrowExceptionWhenTripNotFound() {
        // Given
        when(tripMapper.deleteTrip(1)).thenReturn(0);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> tripService.deleteTrip(1));
        verify(tripMapper, times(1)).deleteTrip(1);
    }
}