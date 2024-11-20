package com.ssafy.enjoytrip.global.exception;

public class ExternalServerTooManyRequestsException extends EnjoyTripException{
    public ExternalServerTooManyRequestsException(ErrorCode errorCode) {
        super(errorCode);
    }
}

