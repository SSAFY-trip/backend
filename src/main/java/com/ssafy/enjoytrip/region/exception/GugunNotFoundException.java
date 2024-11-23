package com.ssafy.enjoytrip.region.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class GugunNotFoundException extends EnjoyTripException {
    public GugunNotFoundException() {
        super(LocationErrorCode.GUGUN_NOT_FOUND);
    }
}