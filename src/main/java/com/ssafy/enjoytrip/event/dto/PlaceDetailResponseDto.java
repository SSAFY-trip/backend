package com.ssafy.enjoytrip.event.dto;

import java.util.List;

import lombok.Getter;

import com.ssafy.enjoytrip.openfeign.dto.TMapPlaceDetailResponseDto;

@Getter
public class PlaceDetailResponseDto {
    private String placeId;
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

    public PlaceDetailResponseDto(TMapPlaceDetailResponseDto tMapDto) {
        TMapPlaceDetailResponseDto.PoiDetailInfo detailInfo = tMapDto.getPoiDetailInfo();

        this.placeId = detailInfo.getId();
        this.name = detailInfo.getName();
        this.category = detailInfo.getCategory();
        this.address = detailInfo.getFullAddress();
        this.roadAddress = detailInfo.getFullRoadAddress();
        this.zipCode = detailInfo.getZipCode();
        this.telephone = detailInfo.getTelephone();
        this.latitude = detailInfo.getLatitude();
        this.longitude = detailInfo.getLongitude();
        this.menus = detailInfo.getMenus();
        this.canParkFlag = detailInfo.getBooleanCanParkFlag();
        this.runsTwentyFourSevenFlag = detailInfo.getBooleanRunsTwentyFourSevenFlag();
        this.runsYearLongFlag = detailInfo.getBooleanRunsYearLongFlag();
        this.homepageUrl = detailInfo.getHomepageUrl();
        this.description = detailInfo.getDescription();
        this.additionalInfo = detailInfo.getAdditionalInfo();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
