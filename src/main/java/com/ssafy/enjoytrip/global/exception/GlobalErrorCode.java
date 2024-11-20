package com.ssafy.enjoytrip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements ErrorCode {
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "GLOABL_400_1", "Invalid data provided"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500_1", "Unexpected error in internal server has occurred."),
//    INVALID_DATA(HttpStatus.BAD_REQUEST, "INVALID_DATA", "Invalid data provided"),
//    MISSING_REQUIRED_FIELD(HttpStatus.BAD_REQUEST, "MISSING_REQUIRED_FIELD", "Required field is missing"),
//    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_FORMAT", "Data format is incorrect"),

    S3_OPERATION_FAILED(HttpStatus.BAD_REQUEST, "S3_400_1", "S3 operation failed."),
    S3_FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "S3_400_2", "S3 file not found."),
    S3_UNEXPECTED_ERROR(HttpStatus.BAD_REQUEST, "S3_400_3", "Unexpected error occurred during S3 operation."),
    ;
    private final HttpStatus status;
    private final String code;
    private final String message;
}
