package com.ssafy.enjoytrip.openfeign.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class TMapClientConfig {
    @Value("${feign.client.api-key.tMap}")
    private String apiKey;

    @Bean
    public RequestInterceptor tMapRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("appKey", apiKey);
            requestTemplate.header("Accept", "application/json");
        };
    }
}
