package com.ssafy.enjoytrip.trip.dto;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoytrip.trip.domain.Trip;

@Builder
@Getter
public class TripCreateDto {
    private String uid;

    @NotBlank(message = "Name is required")
    @Size(max = 15, message = "Name must be at most 15 characters")
    private String name;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Size(max = 100, message = "Trip overview must be at most 100 characters")
    private String tripOverview;

    private Boolean isPublic;

    private String imgUrl;

    @NotNull
    private MultipartFile image;

    public Trip toEntity(){
        return Trip.builder()
                .uid(uid)
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .tripOverview(tripOverview)
                .imgUrl(imgUrl)
                .isPublic(isPublic)
                .build();
    }

    public void createUidAndImgUrlWith(String baseUrl){
        this.uid = UUID.randomUUID().toString();
        this.imgUrl = baseUrl + this.uid;
    }
}
