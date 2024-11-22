package com.ssafy.enjoytrip.login.exception;

import com.ssafy.enjoytrip.event.exception.EventErrorCode;
import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class AccessTokenInvalidException extends EnjoyTripException {
    public AccessTokenInvalidException(){
        super(UserErrorCode.INVALID_ACCESS_TOKEN);
    }
}
