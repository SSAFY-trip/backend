package com.ssafy.enjoytrip.login.Response;

import com.ssafy.enjoytrip.login.domain.AuthProvider;

import java.util.Map;

public class NaverResponse implements OAuth2Response {
    private final Map<String, Object> attribute;
    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }
    @Override
    public AuthProvider getProvider() {
        return AuthProvider.NAVER;
    }
    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }
    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }
    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}