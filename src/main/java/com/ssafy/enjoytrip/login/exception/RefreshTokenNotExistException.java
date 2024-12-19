package com.ssafy.enjoytrip.login.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;
public class RefreshTokenNotExistException extends EnjoyTripException {
    public RefreshTokenNotExistException(){
        super(UserErrorCode.EXPIRED_REFRESH_TOKEN);
    }
}
