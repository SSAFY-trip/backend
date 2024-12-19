package com.ssafy.enjoytrip.global.exception;

public class ExternalInternalServerError extends EnjoyTripException{
    public ExternalInternalServerError(ErrorCode errorCode) {
        super(errorCode);
    }
}
