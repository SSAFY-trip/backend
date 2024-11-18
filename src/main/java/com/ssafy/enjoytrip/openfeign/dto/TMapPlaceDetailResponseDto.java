package com.ssafy.enjoytrip.openfeign.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.StringUtils;

@Getter
public class TMapPlaceDetailResponseDto {
    @JsonProperty("poiDetailInfo")
    private PoiDetailInfo poiDetailInfo;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PoiDetailInfo {
        private String id;

        private String name;

        @JsonProperty("bizCatName")
        private String category;

        private String address;

        @JsonProperty("firstNo")
        private String addressFirstNum;

        @JsonProperty("secondNo")
        private String addressSecondNum;

        private String zipCode;

        @JsonProperty("tel")
        private String telephone;

        @JsonProperty("bldAddr")
        private String roadAddress;

        @JsonProperty("bldNo1")
        private String roadAddressFirstNum;

        @JsonProperty("bldNo2")
        private String roadAddressSecondNum;

        @JsonProperty("lat")
        private Double latitude;

        @JsonProperty("lon")
        private Double longitude;

        private String menu1;

        private String menu2;

        private String menu3;

        private String menu4;

        private String menu5;

        @JsonProperty("parkFlag")
        private Integer canParkFlag;

        @JsonProperty("twFlag")
        private Integer runsTwentyFourSevenFlag;

        @JsonProperty("yaFlag")
        private Integer runsYearLongFlag;

        private String homepageUrl;

        private String additionalInfo;

        @JsonProperty("desc")
        private String description;

        /*
        custom computed fields for easier use
         */
        public String getFullAddress() {
            return address + " " + addressFirstNum + (StringUtils.hasText(addressSecondNum) ? "-" + addressSecondNum : "");
        }

        public String getFullRoadAddress() {
            return roadAddress + " " + roadAddressFirstNum + (StringUtils.hasText(roadAddressSecondNum) ? "-" + roadAddressSecondNum : "");
        }

        public List<String> getMenus() {
            return new ArrayList<String>(Arrays.asList(menu1, menu2, menu3, menu4, menu5))
                    .stream()
                    .filter(menu -> StringUtils.hasText(menu))
                    .collect(Collectors.toList());
        }

        public Boolean getBooleanCanParkFlag() {
            return canParkFlag != null && canParkFlag == 1;
        }

        public Boolean getBooleanRunsTwentyFourSevenFlag() {
            return runsTwentyFourSevenFlag != null && runsTwentyFourSevenFlag == 1;
        }

        public Boolean getBooleanRunsYearLongFlag() {
            return runsYearLongFlag != null && runsYearLongFlag == 1;
        }
    }
}
