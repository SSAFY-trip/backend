package com.ssafy.enjoytrip.login.exception;

import com.ssafy.enjoytrip.event.exception.EventErrorCode;
import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class RefreshTokenExpiredException extends EnjoyTripException {
    public RefreshTokenExpiredException(){
        super(UserErrorCode.EXPIRED_REFRESH_TOKEN);
    }
}
