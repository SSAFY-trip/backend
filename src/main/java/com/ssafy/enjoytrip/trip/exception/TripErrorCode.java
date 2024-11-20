package com.ssafy.enjoytrip.trip.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.ssafy.enjoytrip.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum TripErrorCode implements ErrorCode {
    TRIP_CANNOT_END_BEFORE_START(HttpStatus.BAD_REQUEST,"TRIP_400_1", "Trip start date must be before end date."),
    DATE_NOT_WITHIN_RANGE(HttpStatus.BAD_REQUEST, "TRIP_400_2", "Given date is not within trip's duration"),
    TRIP_DATE_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "TRIP_400_3", "Trip dates conflict with existing events' dates"),

    TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "TRIP_404_1", "Trip not found."),

    USER_IS_UNAUTHORIZED(HttpStatus.FORBIDDEN,"TRIP_403_1", "User is not team of member.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
