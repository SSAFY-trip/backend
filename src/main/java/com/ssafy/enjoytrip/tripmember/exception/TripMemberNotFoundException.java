package com.ssafy.enjoytrip.tripmember.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class TripMemberNotFoundException extends EnjoyTripException {
    public TripMemberNotFoundException() {
        super(TripMemberErrorCode.TRIP_MEMBER_NOT_FOUND);
    }
}
