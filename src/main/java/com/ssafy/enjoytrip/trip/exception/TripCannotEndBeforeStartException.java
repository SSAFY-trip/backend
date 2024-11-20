package com.ssafy.enjoytrip.trip.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class TripCannotEndBeforeStartException extends EnjoyTripException {
    public TripCannotEndBeforeStartException(){
        super(TripErrorCode.TRIP_CANNOT_END_BEFORE_START);
    }
}
