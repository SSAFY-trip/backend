package com.ssafy.enjoytrip.mapper;

import com.ssafy.enjoytrip.domain.Trip;
import com.ssafy.enjoytrip.repository.TripMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Sql(scripts = "/schema.sql")
@Transactional
class TripMapperTest {
    @Autowired
    private TripMapper tripMapper;

    private Trip trip;

    @BeforeEach
    void setUp() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        trip = new Trip();
        trip.setName("Trip to Paris");
        trip.setStartDate(dateFormat.parse("2023-01-01"));
        trip.setEndDate(dateFormat.parse("2023-01-10"));
        trip.setTripOverview("A wonderful trip to Paris.");
        trip.setImgUrl("http://example.com/image.jpg");
        trip.setIsPublic(true);
    }

    @AfterEach
    void tearDown() {
        trip = null;
    }

    @Test
    void insertTrip() {
        tripMapper.insertTrip(trip);
        Trip fetchedTrip = tripMapper.getTripById(trip.getId());
        assertNotNull(fetchedTrip);
        assertEquals("Trip to Paris", fetchedTrip.getName());
    }

    @Test
    void getTripById() {
        tripMapper.insertTrip(trip);
        Trip fetchedTrip = tripMapper.getTripById(trip.getId());
        assertNotNull(fetchedTrip);
        assertEquals(trip.getName(), fetchedTrip.getName());
    }

    @Test
    void getAllTrips() {
        tripMapper.insertTrip(trip);
        List<Trip> trips = tripMapper.getAllTrips();
        assertFalse(trips.isEmpty());
        assertEquals(1, trips.size());
    }

    @Test
    void updateTrip() {
        tripMapper.insertTrip(trip);
        trip.setName("Updated Trip to Paris");
        tripMapper.updateTrip(trip);
        Trip updatedTrip = tripMapper.getTripById(trip.getId());
        assertEquals("Updated Trip to Paris", updatedTrip.getName());
    }

    @Test
    void deleteTrip() {
        tripMapper.insertTrip(trip);
        tripMapper.deleteTrip(trip.getId());
        Trip deletedTrip = tripMapper.getTripById(trip.getId());
        assertNull(deletedTrip);
    }
}