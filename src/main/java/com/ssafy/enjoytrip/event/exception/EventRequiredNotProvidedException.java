package com.ssafy.enjoytrip.event.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class EventRequiredNotProvidedException extends EnjoyTripException {
    public EventRequiredNotProvidedException() {
        super(EventErrorCode.REQUIRED_EVENT_NOT_PROVIDED);
    }
}
