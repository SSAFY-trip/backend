package com.ssafy.enjoytrip.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ssafy.enjoytrip.openfeign.config.KakaoClientConfig;
import com.ssafy.enjoytrip.openfeign.dto.KakaoImageResponseDto;

@FeignClient(name = "KakaoClient",
        url = "https://dapi.kakao.com",
        configuration = KakaoClientConfig.class
)
public interface KakaoClient {
    @GetMapping("/v2/search/image?query={query}")
    KakaoImageResponseDto getImages(@PathVariable String query);
}
