package com.ssafy.enjoytrip.login.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import com.ssafy.enjoytrip.global.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "U001", "Access token has expired."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "U002", "Refresh token has expired."),
    INVALID_ACCESS_TOKEN(HttpStatus.FORBIDDEN, "U003", "Invalid access token."),
    INVALID_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "U004", "Invalid refresh token."),
    NO_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "U005", "refresh token not exist."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "U006", "Failed to parse JSON request.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
