package com.ssafy.enjoytrip.openfeign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoImageResponseDto {
    @JsonProperty("documents")
    private List<Document> documents;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        @JsonProperty("image_url")
        private String imageUrl;
    }

    public List<String> toImageUrlList(){
        List<String> imageUrls = new ArrayList<>();
        for(Document document : documents) {
            imageUrls.add(document.imageUrl);
        }
        return imageUrls;
    }
}
