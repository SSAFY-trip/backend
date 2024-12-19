package com.ssafy.enjoytrip.trip.adaptor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.exception.TripCannotEndBeforeStartException;
import com.ssafy.enjoytrip.trip.exception.TripDateRangeConflictException;
import com.ssafy.enjoytrip.trip.exception.TripNotFoundException;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripAdaptorTest {
    @Mock
    private TripMapper tripMapper;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private TripAdaptor tripAdaptor;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = Trip.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .isPublic(true)
                .build();
    }

    @Test
    @DisplayName("Create Trip - call insertTrip()")
    void testCreateTrip_shouldCallInsertTrip() {
        // When
        tripAdaptor.insertTrip(trip);

        // Then
        verify(tripMapper, times(1)).insertTrip(refEq(trip));
    }

    @Test
    @DisplayName("Create Trip - raise TripCannotEndBeforeStartException")
    void testCreateTrip_raiseTripCannotEndBeforeStartException() {
        // Given
        Trip trip = Trip.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 15))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .isPublic(true)
                .build();

        // When & Then
        assertThrows(TripCannotEndBeforeStartException.class, () -> tripAdaptor.insertTrip(trip));
    }

    @Test
    @DisplayName("Get Trip - call getTripById()")
    void testGetTripById_shouldReturnTripResponseDto() {
        // Given
        when(tripMapper.getTripById(1)).thenReturn(trip);

        // When
        Trip result = tripAdaptor.getTripById(1);

        // Then
        assertNotNull(result, "Trip should not be null");
        assertEquals(trip.getName(), result.getName(), "Trip name should match");
        verify(tripMapper, times(1)).getTripById(1);
    }

    @Test
    @DisplayName("Get Trip - raise TripNotFoundException")
    void testGetTripById_shouldThrowExceptionWhenTripNotFound() {
        // Given
        when(tripMapper.getTripById(1)).thenReturn(null);

        // When & Then
        assertThrows(TripNotFoundException.class, () -> tripAdaptor.getTripById(1));
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
                .isPublic(true)
                .build();

        Trip trip2 = Trip.builder()
                .id(2)
                .name("Rome")
                .startDate(LocalDate.of(2023, 2, 1))
                .endDate(LocalDate.of(2023, 2, 10))
                .tripOverview("An amazing trip to Rome.")
                .isPublic(true)
                .build();

        when(tripMapper.getAllTrips()).thenReturn(Arrays.asList(trip1, trip2));

        // When
        List<Trip> result = tripAdaptor.getAllTrips();

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
        Trip updatedTrip = Trip.builder()
                .id(1)
                .name("Updated Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("An updated trip overview.")
                .isPublic(false)
                .build();

        when(eventMapper.getEventDateRangeOfTripId(updatedTrip.getId())).thenReturn(
                new HashMap<String, LocalDate>() {{
                    put("minDate", null);
                    put("maxDate", null);
                }});
        when(tripMapper.updateTrip(refEq(updatedTrip))).thenReturn(1);

        // When
        tripAdaptor.updateTrip(updatedTrip);

        // Then
        verify(tripMapper, times(1)).updateTrip(refEq(updatedTrip));
    }

    @Test
    @DisplayName("Update Trip - raise TripNotFoundException")
    void testUpdateTrip_shouldThrowExceptionWhenTripNotFound() {
        // Given
        Trip updatedTrip = Trip.builder()
                .id(1)
                .name("Updated Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("An updated trip overview.")
                .isPublic(false)
                .build();

        when(eventMapper.getEventDateRangeOfTripId(updatedTrip.getId())).thenReturn(
                Map.of(
                        "minDate", LocalDate.of(2023, 1, 1),
                        "maxDate", LocalDate.of(2023, 1, 10)));
        when(tripMapper.updateTrip(refEq(updatedTrip))).thenReturn(0);

        // When & Then
        assertThrows(TripNotFoundException.class, () -> tripAdaptor.updateTrip(updatedTrip));
        verify(tripMapper, times(1)).updateTrip(refEq(updatedTrip));
    }

    @Test
    @DisplayName("Update Trip - raise TripDateRangeConflictException")
    void testUpdateTrip_shouldThrowTripDateRangeConflictException() {
        // Given
        Trip updatedTrip = Trip.builder()
                .id(1)
                .name("Updated Paris")
                .startDate(LocalDate.of(2023, 1, 5))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("An updated trip overview.")
                .isPublic(false)
                .build();

        when(eventMapper.getEventDateRangeOfTripId(updatedTrip.getId())).thenReturn(
                Map.of(
                        "minDate", LocalDate.of(2023, 1, 1),
                        "maxDate", LocalDate.of(2023, 1, 10)));

        // When & Then
        assertThrows(TripDateRangeConflictException.class, () -> tripAdaptor.updateTrip(updatedTrip));
        verify(tripMapper, never()).updateTrip(refEq(updatedTrip));
    }


    @Test
    @DisplayName("Delete Trip - calls deleteById()")
    void deleteTrip_shouldDeleteTrip() {
        // Given
        when(tripMapper.deleteTrip(1)).thenReturn(1);

        // When
        tripAdaptor.deleteTrip(1);

        // Then
        verify(tripMapper, times(1)).deleteTrip(1);
    }

    @Test
    @DisplayName("Delete Trip - raise TripNotFoundException")
    void deleteTrip_shouldThrowTripNotFoundException() {
        // Given
        when(tripMapper.deleteTrip(1)).thenReturn(0);

        // When & Then
        assertThrows(TripNotFoundException.class, () -> tripAdaptor.deleteTrip(1));
        verify(tripMapper, times(1)).deleteTrip(1);
    }
}