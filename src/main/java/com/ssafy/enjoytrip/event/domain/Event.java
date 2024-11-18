package com.ssafy.enjoytrip.event.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
@Getter
public class Event {
    private Integer id;

    private Integer tripId;

    @NotBlank
    @NotNull
    private String name;

    @NotNull
    private LocalDate date;

    @NotNull
    private Integer order;

    private String memo;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private String category;

    private String placeId;
}
