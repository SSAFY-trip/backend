package com.ssafy.enjoytrip.openfeign.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class TMapPlaceSearchResponseDto {
    private SearchPoiInfo searchPoiInfo;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchPoiInfo {
        @JsonProperty("pois")
        private PoiJson poiJson;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PoiJson {
            @JsonProperty("poi")
            private List<Poi> pois;

            @Getter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Poi {
                private String id;
            }
        }
    }

    /*
    Utility methods
     */
    public List<String> toPlaceIdList() {
        return searchPoiInfo.poiJson.pois.stream()
                .map(SearchPoiInfo.PoiJson.Poi::getId)
                .distinct()
                .collect(Collectors.toList());
    }
}

