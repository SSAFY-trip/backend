package com.ssafy.enjoytrip.global.exception;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ssafy.enjoytrip.global.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(EnjoyTripException.class)
    public ResponseEntity<Object> handleEnjoyTripException(EnjoyTripException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ErrorResponse.toResponseEntity(GlobalErrorCode.VALIDATION_FAILED, errors);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        return ErrorResponse.toResponseEntity((HttpStatus) statusCode);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> ConstraintViolationExceptionHandler(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(violation -> {
                    errors.put(violation.getPropertyPath().toString(), violation.getMessage());
                });

        return ErrorResponse.toResponseEntity(GlobalErrorCode.VALIDATION_FAILED, errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ErrorResponse.toResponseEntity(GlobalErrorCode.INTERNAL_SERVER_ERROR);
    }
}
