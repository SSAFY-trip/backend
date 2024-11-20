package com.ssafy.enjoytrip.global.exception;

public class ExternalServerNotFoundException extends EnjoyTripException{
    public ExternalServerNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
