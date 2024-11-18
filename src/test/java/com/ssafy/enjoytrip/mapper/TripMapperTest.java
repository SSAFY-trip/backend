package com.ssafy.enjoytrip.mapper;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ssafy.enjoytrip.trip.domain.Trip;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TripMapperTest {
    @Autowired
    private TripMapper tripMapper;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = Trip.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .imgUrl("http://example.com/image.jpg")
                .isPublic(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        tripMapper.deleteAllTrips();
        tripMapper.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test insert trip & auto-incremented id")
    void testInsertTrip() {
        tripMapper.insertTrip(trip);

        // test trip insertion
        Trip fetchedTrip = tripMapper.getTripById(trip.getId());
        assertNotNull(fetchedTrip, "Fetched trip should not be null");
        assertNotNull(fetchedTrip.getId(), "Trip ID should be generated");
        assertEquals(trip.getName(), fetchedTrip.getName(), "Trip name should match");

        // test auto-increment id
        tripMapper.insertTrip(trip);
        fetchedTrip = tripMapper.getTripById(trip.getId());
        assertEquals(fetchedTrip.getId(), trip.getId(), "Trip ID should be auto-incremented");
    }

    @Test
    @DisplayName("Test get trip by id")
    void testGetTripById() {
        tripMapper.insertTrip(trip);
        Trip fetchedTrip = tripMapper.getTripById(trip.getId());
        assertNotNull(fetchedTrip, "Fetched trip should not be null");
        assertEquals(trip.getName(), fetchedTrip.getName(), "Trip name should match");
    }

    @Test
    @DisplayName("Test get all trip list")
    void testGetAllTrips() {
        tripMapper.insertTrip(trip);
        tripMapper.insertTrip(trip);
        tripMapper.insertTrip(trip);
        List<Trip> trips = tripMapper.getAllTrips();
        assertFalse(trips.isEmpty(), "Trip list should not be empty");
        assertEquals(3, trips.size(), "Trip list size should be 3");
    }

    @Test
    @DisplayName("Test update trip")
    void testUpdateTrip() {
        tripMapper.insertTrip(trip);

        Trip updatedTrip = Trip.builder()
                .id(trip.getId())
                .name("Updated Trip")
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .tripOverview(trip.getTripOverview())
                .imgUrl(trip.getImgUrl())
                .isPublic(false)
                .build();

        int rowsAffected = tripMapper.updateTrip(updatedTrip);
        assertEquals(1, rowsAffected, "One row should be affected on update");

        Trip fetchedTrip = tripMapper.getTripById(trip.getId());
        assertEquals("Updated Trip", fetchedTrip.getName(), "Trip name should be updated");
        assertFalse(fetchedTrip.getIsPublic(), "Public status should be false after update");
    }

    @Test
    @DisplayName("Test delete trip by id")
    void testDeleteTrip() {
        tripMapper.insertTrip(trip);
        int rowsAffected = tripMapper.deleteTrip(trip.getId());
        assertEquals(1, rowsAffected, "One row should be affected on delete");

        Trip deletedTrip = tripMapper.getTripById(trip.getId());
        assertNull(deletedTrip, "Deleted trip should be null");
    }
}
