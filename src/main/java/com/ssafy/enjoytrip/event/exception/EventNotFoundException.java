package com.ssafy.enjoytrip.event.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class EventNotFoundException extends EnjoyTripException {
    public EventNotFoundException(){
        super(EventErrorCode.EVENT_NOT_FOUND);
    }
}
