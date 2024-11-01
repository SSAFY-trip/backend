package com.ssafy.enjoytrip.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = new Trip();
    }

    @AfterEach
    void tearDown() {
        trip = null;
    }

    @Test
    void getId() {
        trip.setId(1);
        assertEquals(1, trip.getId());
    }

    @Test
    void setId() {
        trip.setId(2);
        assertEquals(2, trip.getId());
    }

    @Test
    void getName() {
        trip.setName("Trip to Paris");
        assertEquals("Trip to Paris", trip.getName());
    }

    @Test
    void setName() {
        trip.setName("Trip to London");
        assertEquals("Trip to London", trip.getName());
    }

    @Test
    void getStartDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = dateFormat.parse("2023-01-01");
        trip.setStartDate(expectedDate);
        assertEquals(expectedDate, trip.getStartDate());
    }

    @Test
    void setStartDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = dateFormat.parse("2023-02-01");
        trip.setStartDate(newDate);
        assertEquals(newDate, trip.getStartDate());
    }

    @Test
    void getEndDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date expectedDate = dateFormat.parse("2023-01-10");
        trip.setEndDate(expectedDate);
        assertEquals(expectedDate, trip.getEndDate());
    }

    @Test
    void setEndDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = dateFormat.parse("2023-02-10");
        trip.setEndDate(newDate);
        assertEquals(newDate, trip.getEndDate());
    }

    @Test
    void getTripOverview() {
        trip.setTripOverview("A wonderful trip to Paris.");
        assertEquals("A wonderful trip to Paris.", trip.getTripOverview());
    }

    @Test
    void setTripOverview() {
        trip.setTripOverview("An amazing trip to London.");
        assertEquals("An amazing trip to London.", trip.getTripOverview());
    }

    @Test
    void getImgUrl() {
        trip.setImgUrl("http://example.com/image.jpg");
        assertEquals("http://example.com/image.jpg", trip.getImgUrl());
    }

    @Test
    void setImgUrl() {
        trip.setImgUrl("http://example.com/another-image.jpg");
        assertEquals("http://example.com/another-image.jpg", trip.getImgUrl());
    }

    @Test
    void getIsPublic() {
        trip.setIsPublic(true);
        assertTrue(trip.getIsPublic());
    }

    @Test
    void setIsPublic() {
        trip.setIsPublic(false);
        assertFalse(trip.getIsPublic());
    }
}