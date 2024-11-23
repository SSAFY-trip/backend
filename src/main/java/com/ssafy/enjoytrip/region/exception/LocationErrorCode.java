package com.ssafy.enjoytrip.region.exception;

import com.ssafy.enjoytrip.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LocationErrorCode implements ErrorCode {
    SIDO_NOT_FOUND(HttpStatus.NOT_FOUND, "SIDO_404", "Sido not found."),
    GUGUN_NOT_FOUND(HttpStatus.NOT_FOUND, "GUGUN_404", "Gugun not found."),
    SIDO_GUGUN_MISMATCH(HttpStatus.BAD_REQUEST, "SIDO_GUGUN_400", "Sido and Gugun mismatch."),
    SIDO_ALREADY_EXISTS(HttpStatus.CONFLICT, "LOC_409_1", "Sido already exists.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
