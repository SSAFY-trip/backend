package com.ssafy.enjoytrip.trip.dto;

import lombok.Builder;
import lombok.Getter;

import com.ssafy.enjoytrip.trip.domain.Trip;


@Getter
@Builder
public class TripResponseDto {
    private Integer id;
    private String uid;
    private String name;
    private String startDate;
    private String endDate;
    private String tripOverview;
    private String imgUrl;
    private Boolean isPublic;

    public static TripResponseDto of(Trip trip) {
        return TripResponseDto.builder()
                .id(trip.getId())
                .uid(trip.getUid())
                .name(trip.getName())
                .startDate(trip.getStartDate().toString())
                .endDate(trip.getEndDate().toString())
                .tripOverview(trip.getTripOverview())
                .imgUrl(trip.getImgUrl())
                .isPublic(trip.getIsPublic())
                .build();
    }
}
