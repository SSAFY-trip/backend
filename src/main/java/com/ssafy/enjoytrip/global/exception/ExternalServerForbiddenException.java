package com.ssafy.enjoytrip.global.exception;

public class ExternalServerForbiddenException extends EnjoyTripException{
    public ExternalServerForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
