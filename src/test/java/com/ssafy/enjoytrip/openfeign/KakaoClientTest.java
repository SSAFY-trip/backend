package com.ssafy.enjoytrip.openfeign;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.ssafy.enjoytrip.openfeign.dto.KakaoImageResponseDto;

@SpringBootTest
@ActiveProfiles("test")
public class KakaoClientTest {
    @Autowired
    KakaoClient kakaoClient;

    @Test
    @DisplayName("Test Image Search API")
    void testImageSearch() throws Exception {
        String query = "미쁜식당";
        KakaoImageResponseDto dto = kakaoClient.getImages(query);

        System.out.println(dto.toImageUrlList());
    }
}
