package com.ssafy.enjoytrip.event.dto;

import java.time.LocalDate;

import com.ssafy.enjoytrip.domain.Event;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
@Getter
public class EventCreateDto {
    @NotBlank(message = "Name can't be blank")
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Size(max = 300, message = "Memo must be at most 300 characters")
    private String memo;

    @NotNull(message = "Latitude is required")
    private Float latitude;

    @NotNull(message = "Longitude is required")
    private Float longitude;

    private String category;

    public Event toEntity(){
        return Event.builder()
                .name(name)
                .date(date)
                .memo(memo)
                .latitude(latitude)
                .longitude(longitude)
                .category(category)
                .build();
    }
}
