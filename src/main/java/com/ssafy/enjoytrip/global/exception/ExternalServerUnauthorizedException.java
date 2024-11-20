package com.ssafy.enjoytrip.global.exception;

public class ExternalServerUnauthorizedException extends EnjoyTripException{
    public ExternalServerUnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
