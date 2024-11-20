package com.ssafy.enjoytrip.global.exception;

public class ExternalServerUnexpectedErrorException extends EnjoyTripException{
    public ExternalServerUnexpectedErrorException(ErrorCode errorCode) {
        super(errorCode);
    }
}
