package com.ssafy.enjoytrip.dto;

import java.time.LocalDate;

import lombok.Getter;

import com.ssafy.enjoytrip.domain.Trip;

@Getter
public class TripResponseDto {
    private Integer id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String tripOverview;
    private String imgUrl;
    private Boolean isPublic;

    public Trip toEntity() {
        return Trip.builder()
                .id(id)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .tripOverview(tripOverview)
                .imgUrl(imgUrl)
                .isPublic(isPublic)
                .build();
    }

    public TripResponseDto(Trip trip) {
        this.id = trip.getId();
        this.name = trip.getName();
        this.startDate = trip.getStartDate();
        this.endDate = trip.getEndDate();
        this.tripOverview = trip.getTripOverview();
        this.imgUrl = trip.getImgUrl();
        this.isPublic = trip.getIsPublic();
    }
}
