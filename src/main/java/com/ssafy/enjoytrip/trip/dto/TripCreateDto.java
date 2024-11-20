package com.ssafy.enjoytrip.trip.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.ssafy.enjoytrip.trip.domain.Trip;

@Builder
@Getter
public class TripCreateDto {
    @NotBlank(message = "Name is required")
    @Size(max = 15, message = "Name must be at most 15 characters")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Size(max = 100, message = "Trip overview must be at most 100 characters")
    private String tripOverview;

    @Size(max = 50, message = "Image URL must be at most 50 characters")
    private String imgUrl;

    private Boolean isPublic;

    public Trip toEntity(){
        return Trip.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .tripOverview(tripOverview)
                .imgUrl(imgUrl)
                .isPublic(isPublic)
                .build();
    }
}
