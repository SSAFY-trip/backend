package com.ssafy.enjoytrip.trip.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class TripNotFoundException extends EnjoyTripException {
    public TripNotFoundException(){
        super(TripErrorCode.TRIP_NOT_FOUND);
    }
}
