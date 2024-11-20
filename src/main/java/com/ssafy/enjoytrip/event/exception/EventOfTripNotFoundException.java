package com.ssafy.enjoytrip.event.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class EventOfTripNotFoundException extends EnjoyTripException {
    public EventOfTripNotFoundException() {
        super(EventErrorCode.EVENT_OF_TRIP_NOT_FOUND);
    }
}
