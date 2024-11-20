package com.ssafy.enjoytrip.trip.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

import com.ssafy.enjoytrip.trip.domain.Trip;

@Getter
@Builder
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

    public static TripResponseDto of(Trip trip) {
        return TripResponseDto.builder()
                .id(trip.getId())
                .name(trip.getName())
                .startDate(trip.getStartDate())
                .endDate(trip.getEndDate())
                .tripOverview(trip.getTripOverview())
                .imgUrl(trip.getImgUrl())
                .isPublic(trip.getIsPublic())
                .build();
    }
}
