package com.ssafy.enjoytrip.event.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class EventOfTripAndDateNotFoundException extends EnjoyTripException {
    public EventOfTripAndDateNotFoundException(){
        super(EventErrorCode.EVENT_OF_TRIP_AND_DATE_NOT_FOUND);
    }
}
