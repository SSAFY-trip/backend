package com.ssafy.enjoytrip.event.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class EventMaxCntException extends EnjoyTripException {
    public EventMaxCntException(){
        super(EventErrorCode.MAX_EVENT_CNT_REACHED);
    }
}
