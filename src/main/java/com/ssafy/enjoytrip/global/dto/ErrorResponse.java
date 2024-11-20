package com.ssafy.enjoytrip.global.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ssafy.enjoytrip.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> errors;

    public static ResponseEntity<Object> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .build()
                );
    }

    public static ResponseEntity<Object> toResponseEntity(HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(
                        ErrorResponse.builder()
                                .code(String.format("GLOBAL_%s_%s", status.value(), status.name()))
                                .message(status.getReasonPhrase())
                                .build()
                );
    }

    public static ResponseEntity<Object> toResponseEntity(ErrorCode errorCode, Map<String, String> errors) {
        Map<String, String> message = new HashMap<>();
        message.put("code", errorCode.getCode());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(
                        ErrorResponse.builder()
                                .code(errorCode.getCode())
                                .message(errorCode.getMessage())
                                .errors(errors)
                                .build()
                );
    }

}
