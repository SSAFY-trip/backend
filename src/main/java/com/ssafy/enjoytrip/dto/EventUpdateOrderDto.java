package com.ssafy.enjoytrip.dto;

import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;

import com.ssafy.enjoytrip.domain.Event;


@Builder
@Getter
public class EventUpdateOrderDto {
    @NotNull
    private Integer id;

    private Integer order;

    public void setUpdateOrder(Integer order){
        this.order = order;
    }

    public Event toEntity() {
        return Event.builder()
                .id(id)
                .order(order)
                .build();
    }
}
