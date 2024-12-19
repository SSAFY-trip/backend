package com.ssafy.enjoytrip.event.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import com.ssafy.enjoytrip.event.domain.Event;

@Builder
@Getter
@Jacksonized
public class RouteOptimizationRequestDto {
    private Integer startEventId;
    private Double startEventLongitude;
    private Double startEventLatitude;
    private Integer endEventId;
    private Double endEventLongitude;
    private Double endEventLatitude;

    public List<Event> toEventList() {
        List<Event> events = new ArrayList<>();
        events.add(Event.builder()
                .id(startEventId)
                .latitude(startEventLatitude)
                .longitude(startEventLongitude)
                .build());
        events.add(Event.builder()
                .id(this.endEventId)
                .latitude(endEventLatitude)
                .longitude(endEventLongitude)
                .build());
        return events;
    }
}
