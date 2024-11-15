package com.ssafy.enjoytrip.login.Response;


import com.ssafy.enjoytrip.login.domain.AuthProvider;

public interface OAuth2Response {
    AuthProvider getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}
