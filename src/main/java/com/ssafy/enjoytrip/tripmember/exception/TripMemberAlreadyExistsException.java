package com.ssafy.enjoytrip.tripmember.exception;

import com.ssafy.enjoytrip.global.exception.EnjoyTripException;

public class TripMemberAlreadyExistsException extends EnjoyTripException {
    public TripMemberAlreadyExistsException() {
        super(TripMemberErrorCode.TRIP_MEMBER_ALREADY_EXISTS);
    }
}
