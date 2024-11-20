package com.ssafy.enjoytrip.openfeign.exception;

import com.ssafy.enjoytrip.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FeignErrorCode implements ErrorCode {
    EXTERNAL_SERVER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "FEIGN_400_1", "Request to external server bad request."),
    EXTERNAL_SERVER_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "FEIGN_400_2", "Request to external server unauthorized."),
    EXTERNAL_INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "FEIGN_400_3", "External internal server error."),
    EXTERNAL_SERVER_TOO_MANY_REQUESTS(HttpStatus.BAD_REQUEST, "FEIGN_400_4", "Too many requests to external server."),
    EXTERNAL_SERVER_NOT_FOUND(HttpStatus.BAD_REQUEST, "FEIGN_400_5", "Request to external server not found error."),
    EXTERNAL_SERVER_FORBIDDEN(HttpStatus.BAD_REQUEST, "FEIGN_400_6", "Request to external server forbidden error."),
    EXTERNAL_SERVER_UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, "FEIGN_400_7", "External server unexpected error."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
