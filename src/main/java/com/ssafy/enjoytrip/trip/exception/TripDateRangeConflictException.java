package com.ssafy.enjoytrip.trip.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class TripDateRangeConflictException extends EnjoyTripException {
    public TripDateRangeConflictException(){
        super(TripErrorCode.TRIP_DATE_CANNOT_BE_MODIFIED);
    }
}
