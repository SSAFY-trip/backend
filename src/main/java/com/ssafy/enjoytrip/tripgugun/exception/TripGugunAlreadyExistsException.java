package com.ssafy.enjoytrip.tripgugun.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class TripGugunAlreadyExistsException extends EnjoyTripException {
    public TripGugunAlreadyExistsException() {
        super(TripGugunErrorCode.TRIP_GUGUN_ALREADY_EXISTS);
    }
}
