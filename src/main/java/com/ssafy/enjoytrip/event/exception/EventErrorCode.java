package com.ssafy.enjoytrip.event.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.ssafy.enjoytrip.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements ErrorCode {
    EVENT_DATE_NOT_POSSIBLE(HttpStatus.BAD_REQUEST, "EVENT_400_1", "Event date is not within trip date range."),
    MAX_EVENT_CNT_REACHED(HttpStatus.BAD_REQUEST, "EVENT_400_2", "Only 20 events can be created in each (trip, date)"),
    REQUIRED_EVENT_NOT_PROVIDED(HttpStatus.BAD_REQUEST, "EVENT_400_3", "Required event is not provided."),

    EVENT_OPERATION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "EVENT_403_1", "User is not authorized for this operation that requires leader status."),

    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "EVENT_404_1", "Event not found."),
    EVENT_OF_TRIP_NOT_FOUND(HttpStatus.NOT_FOUND, "EVENT_404_2", "Event of provided trip not found."),
    EVENT_OF_TRIP_AND_DATE_NOT_FOUND(HttpStatus.NOT_FOUND, "EVENT_404_3", "Event of provided trip and date not found.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
