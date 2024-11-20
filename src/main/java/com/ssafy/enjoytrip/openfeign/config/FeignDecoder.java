package com.ssafy.enjoytrip.openfeign.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import com.ssafy.enjoytrip.global.exception.*;
import com.ssafy.enjoytrip.openfeign.exception.FeignErrorCode;

@Component
public class FeignDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        if(400 <= response.status()  && response.status() < 500) {
            switch (response.status()) {
                case 401:
                    return new ExternalServerUnauthorizedException(FeignErrorCode.EXTERNAL_SERVER_UNAUTHORIZED);
                case 403:
                    return new ExternalServerForbiddenException(FeignErrorCode.EXTERNAL_SERVER_FORBIDDEN);
                case 404:
                    return new ExternalServerNotFoundException(FeignErrorCode.EXTERNAL_SERVER_NOT_FOUND);
                case 429:
                    return new ExternalServerTooManyRequestsException(FeignErrorCode.EXTERNAL_SERVER_TOO_MANY_REQUESTS);
                default:
                    return new ExternalServerBadRequestException(FeignErrorCode.EXTERNAL_SERVER_BAD_REQUEST);
            }
        }
        else if(500 <= response.status() && response.status() < 600) {
            return new ExternalInternalServerError(FeignErrorCode.EXTERNAL_INTERNAL_SERVER_ERROR);
        }

        return new ExternalServerUnexpectedErrorException(FeignErrorCode.EXTERNAL_SERVER_UNEXPECTED_ERROR);
    }
}
