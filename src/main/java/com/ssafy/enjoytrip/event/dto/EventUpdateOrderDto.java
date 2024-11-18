package com.ssafy.enjoytrip.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import com.ssafy.enjoytrip.event.domain.Event;

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

    @Builder
    @Getter
    @Jacksonized
    public static class SearchRequestDto {
        @NotNull
        @NotBlank
        private String query;

        private Integer areaLLCode;

        private Integer areaLMCode;

        private Integer radius;

        private Double centerLon;

        private Double centerLat;

        private Integer page;
    }
}
