package com.ssafy.enjoytrip.trip.exception;

import com.ssafy.enjoytrip.event.exception.EventErrorCode;
import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class DateNotWithinTripDurationException extends EnjoyTripException {
    public DateNotWithinTripDurationException(){
        super(EventErrorCode.EVENT_DATE_NOT_POSSIBLE);
    }
}
