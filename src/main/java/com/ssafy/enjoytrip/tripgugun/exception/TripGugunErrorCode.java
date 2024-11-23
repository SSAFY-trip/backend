package com.ssafy.enjoytrip.tripgugun.exception;

import com.ssafy.enjoytrip.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TripGugunErrorCode implements ErrorCode {
    TRIP_GUGUN_ALREADY_EXISTS(HttpStatus.CONFLICT, "TG_409_1", "Trip-Gugun relationship already exists."),
    TRIP_GUGUN_NOT_FOUND(HttpStatus.NOT_FOUND, "TG_404_1", "Trip-Gugun relationship not found.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
