package com.ssafy.enjoytrip.login.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class AccessTokenExpiredException extends EnjoyTripException {
    public AccessTokenExpiredException(){
        super(UserErrorCode.EXPIRED_ACCESS_TOKEN);
    }
}
