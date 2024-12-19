package com.ssafy.enjoytrip.openfeign.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import lombok.Builder;
import lombok.Getter;

import com.ssafy.enjoytrip.event.domain.Event;

@Builder
@Getter
public class TMapRouteOptimizationRequestDto {
    private String startName;
    private String startX;
    private String startY;

    @Builder.Default
    private String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    private String endName;
    private String endX;
    private String endY;

    @Builder.Default
    private String carType = "1";
    private List<ViaPoint> viaPoints;

    @Builder
    @Getter
    public static class ViaPoint{
        private String viaPointId; // event id
        private String viaPointName; // event id
        private String viaX;
        private String viaY;
        private String viaPoiId;

        static ViaPoint toViaPoint(Event event){
            ViaPoint viaPoint = ViaPoint.builder()
                    .viaPointId(String.valueOf(event.getId()))
                    .viaPointName(String.valueOf(event.getId()))
                    .viaX(String.valueOf(event.getLongitude()))
                    .viaY(String.valueOf(event.getLatitude()))
                    .viaPointId(event.getPlaceId())
                    .build();
            return viaPoint;
        }
    }

    public static TMapRouteOptimizationRequestDto toDto(List<Event> events, List<Event> startEndEvents) {
        Set<Integer> startEndEventIds = new HashSet<>(Arrays.asList(startEndEvents.get(0).getId(), startEndEvents.get(1).getId()));
        List<ViaPoint> viaPoints = new ArrayList<>();

        for (Event event : events) {
            if (startEndEventIds.contains(event.getId())) {
                continue;
            }
            viaPoints.add(ViaPoint.toViaPoint(event));
        }

        TMapRouteOptimizationRequestDto dto = TMapRouteOptimizationRequestDto.builder()
                .startName(String.valueOf(startEndEvents.get(0).getId()))
                .startX(String.valueOf(startEndEvents.get(0).getLongitude()))
                .startY(String.valueOf(startEndEvents.get(0).getLatitude()))
                .endName(String.valueOf(startEndEvents.get(1).getId()))
                .endX(String.valueOf(startEndEvents.get(1).getLongitude()))
                .endY(String.valueOf(startEndEvents.get(1).getLatitude()))
                .viaPoints(viaPoints)
                .build();

        return dto;
    }
}
