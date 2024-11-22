package com.ssafy.enjoytrip.login.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class LoginJsonParsingException extends EnjoyTripException {
    public LoginJsonParsingException(){
        super(UserErrorCode.EXPIRED_ACCESS_TOKEN);
    }
}
