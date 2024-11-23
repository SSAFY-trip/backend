package com.ssafy.enjoytrip.region.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class SidoGugunMismatchException extends EnjoyTripException {
    public SidoGugunMismatchException() {
        super(LocationErrorCode.SIDO_GUGUN_MISMATCH);
    }
}