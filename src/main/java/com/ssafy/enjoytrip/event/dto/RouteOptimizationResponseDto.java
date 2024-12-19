package com.ssafy.enjoytrip.event.dto;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;

import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationResponseDto;

@Builder
@Getter
public class RouteOptimizationResponseDto {
    private List<Integer> optimizedOrderOfEventIds;
    private List<TMapRouteOptimizationResponseDto.Feature> mapInfo;

    public static RouteOptimizationResponseDto of(List<Event> optimizedEvents, TMapRouteOptimizationResponseDto tMapRouteOptimizationResponseDto){
        return RouteOptimizationResponseDto.builder()
                .optimizedOrderOfEventIds(optimizedEvents.stream().map(Event::getId).collect(Collectors.toList()))
                .mapInfo(tMapRouteOptimizationResponseDto.getFeatures())
                .build();
    }
}
