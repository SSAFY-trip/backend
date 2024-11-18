package com.ssafy.enjoytrip.openfeign.dto;

import java.util.List;

public class PlaceSimpleInfoDto {
    private String id;
    private String name;
    private String category;
    private String address;
    private String roadAddress;
    private String zipCode;
    private String telephone;
    private Double latitude;
    private Double longitude;
    private List<String> menus;
    private Boolean canParkFlag;
    private Boolean runsTwentyFourSevenFlag;
    private Boolean runsYearLongFlag;
    private String homepageUrl;
    private String description;
    private String additionalInfo;
    private List<String> imageUrls;
}
