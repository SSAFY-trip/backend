package com.ssafy.enjoytrip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EnjoyTripException extends RuntimeException{
    private ErrorCode errorCode;
}
