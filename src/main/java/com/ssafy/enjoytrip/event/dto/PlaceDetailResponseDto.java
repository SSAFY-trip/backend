package com.ssafy.enjoytrip.event.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

import com.ssafy.enjoytrip.openfeign.dto.TMapPlaceDetailResponseDto;

@Builder
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

    public static PlaceDetailResponseDto of(TMapPlaceDetailResponseDto tMapDto){
        TMapPlaceDetailResponseDto.PoiDetailInfo detailInfo = tMapDto.getPoiDetailInfo();

        return PlaceDetailResponseDto.builder()
                .placeId(detailInfo.getId())
                .name(detailInfo.getName())
                .category(detailInfo.getCategory())
                .address(detailInfo.getFullAddress())
                .roadAddress(detailInfo.getFullRoadAddress())
                .zipCode(detailInfo.getZipCode())
                .telephone(detailInfo.getTelephone())
                .latitude(detailInfo.getLatitude())
                .longitude(detailInfo.getLongitude())
                .menus(detailInfo.getMenus())
                .canParkFlag(detailInfo.getBooleanCanParkFlag())
                .runsTwentyFourSevenFlag(detailInfo.getBooleanRunsTwentyFourSevenFlag())
                .runsYearLongFlag(detailInfo.getBooleanRunsYearLongFlag())
                .homepageUrl(detailInfo.getHomepageUrl())
                .description(detailInfo.getDescription())
                .additionalInfo(detailInfo.getAdditionalInfo())
                .build();
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
