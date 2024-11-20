package com.ssafy.enjoytrip.global.exception;

public class InvalidDataException extends EnjoyTripException {
    public InvalidDataException(ErrorCode errorCode) {
        super(errorCode);
    }
}
