package com.ssafy.enjoytrip.tripmember.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.ssafy.enjoytrip.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum TripMemberErrorCode implements ErrorCode {
    TRIP_MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "TRIP_MEMBER_409_1", "Trip member already exists."),
    TRIP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "TRIP_MEMBER_404_1", "Trip member not found.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
