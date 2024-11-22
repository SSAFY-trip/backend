package com.ssafy.enjoytrip.trip.domain;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.*;

class TripTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Test invalid trip names (too long, not blank, not null)")
    void testInvalidTripName() {
        // Invalid trip with a long name
        Trip invalidTripWithLongName = Trip.builder()
                .id(null)
                .name("This name is definitely too long")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("Overview")
                .isPublic(true)
                .build();

        Set<ConstraintViolation<Trip>> violations = validator.validate(invalidTripWithLongName);
        assertFalse(violations.isEmpty(), "Should have validation violations for the trip name length");

        // Invalid trip with blank name
        Trip invalidTripWithBlankName = Trip.builder()
                .id(null)
                .name("")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("Overview")
                .isPublic(true)
                .build();

        violations = validator.validate(invalidTripWithBlankName);
        assertFalse(violations.isEmpty(), "Should have validation violations for blank trip name");

        // Invalid trip with null name
        Trip invalidTripWithNullName = Trip.builder()
                .id(null)
                .name(null)
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("Overview")
                .isPublic(true)
                .build();

        violations = validator.validate(invalidTripWithNullName);
        assertFalse(violations.isEmpty(), "Should have validation violations for null trip name");
    }

    @Test
    @DisplayName("Test invalid start date (not null)")
    void testInvalidStartDate() {
        // Invalid trip with null start date
        Trip invalidTrip = Trip.builder()
                .id(null)
                .name("Trip to Paris")
                .startDate(null)
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("Overview")
                .isPublic(true)
                .build();

        Set<ConstraintViolation<Trip>> violations = validator.validate(invalidTrip);
        assertFalse(violations.isEmpty(), "Should have validation violations for null start date");
    }
}