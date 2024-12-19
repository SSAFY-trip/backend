package com.ssafy.enjoytrip.global.exception;

public class ExternalServerBadRequestException extends EnjoyTripException{
    public ExternalServerBadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }
}
