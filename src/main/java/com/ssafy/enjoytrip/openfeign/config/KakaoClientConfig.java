package com.ssafy.enjoytrip.openfeign.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KakaoClientConfig {
    @Value("${feign.client.api-key.kakao}")
    private String apiKey;

    @Bean
    public RequestInterceptor kakaoRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "KakaoAK " + apiKey);
            requestTemplate.header("Accept", "application/json");
        };
    }
}
