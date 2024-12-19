package com.ssafy.enjoytrip.login.exception;

import com.ssafy.enjoytrip.event.exception.EventErrorCode;
import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class RefreshTokenInvalidException extends EnjoyTripException {
    public RefreshTokenInvalidException() {
        super(UserErrorCode.INVALID_REFRESH_TOKEN);
    }
}
