package com.ssafy.enjoytrip.event.dto;

import com.ssafy.enjoytrip.event.domain.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class EventResponseDto {
    private Integer id;

    private String name;

    private LocalDate date;

    private Integer order;

    private String memo;

    private Double latitude;

    private Double longitude;

    private String category;

    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.date = event.getDate();
        this.order = event.getOrder();
        this.memo = event.getMemo();
        this.latitude = event.getLatitude();
        this.longitude = event.getLongitude();
        this.category = event.getCategory();
    }

    public static EventResponseDto of(Event event) {
        return EventResponseDto.builder()
                .id(event.getId())
                .name(event.getName())
                .date(event.getDate())
                .order(event.getOrder())
                .memo(event.getMemo())
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .category(event.getCategory())
                .build();
    }
}
