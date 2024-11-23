package com.ssafy.enjoytrip.region.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class SidoAlreadyExistsException extends EnjoyTripException {
    public SidoAlreadyExistsException() {
        super(LocationErrorCode.SIDO_ALREADY_EXISTS);
    }
}