package com.ssafy.enjoytrip.trip.domain;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ToString
@Builder
@Getter
public class Trip {
    private Integer id;

    @NotBlank
    @Size(max = 15)
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Size(max = 100)
    private String tripOverview;

    @Size(max = 50)
    private String imgUrl;

    private Boolean isPublic;
}
