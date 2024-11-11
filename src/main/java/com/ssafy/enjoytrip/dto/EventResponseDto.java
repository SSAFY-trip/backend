package com.ssafy.enjoytrip.dto;

import com.ssafy.enjoytrip.domain.Event;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EventResponseDto {
    private Integer id;

    private String name;

    private LocalDate date;

    private Integer order;

    private String memo;

    private Float latitude;

    private Float longitude;

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
}
