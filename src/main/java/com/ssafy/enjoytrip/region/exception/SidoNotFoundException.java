package com.ssafy.enjoytrip.region.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class SidoNotFoundException extends EnjoyTripException {
    public SidoNotFoundException() {
        super(LocationErrorCode.SIDO_NOT_FOUND);
    }
}