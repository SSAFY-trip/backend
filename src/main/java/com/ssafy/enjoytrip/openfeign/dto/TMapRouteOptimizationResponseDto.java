package com.ssafy.enjoytrip.openfeign.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.openfeign.deserializer.GeometryDeserializer;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TMapRouteOptimizationResponseDto {
    @JsonProperty("features")
    private List<Feature> features;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature{
        private Geometry geometry;
        private Properties properties;

        @Getter
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonDeserialize(using = GeometryDeserializer.class)
        public static class Geometry {
            private String type;
            private Object coordinates;
        }

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Properties{
            private String index;
            private String viaPointId;
            private String viaPointName;
        }

        Event toEventEntity(){
            Properties properties = this.properties;
            return Event.builder()
                    .id(Integer.parseInt(properties.viaPointName.substring(4)))
                    .order(Integer.parseInt(properties.index) + 1)
                    .build();
        }
    }

    public List<Event> toEventList(){
        List<Event> events = new ArrayList<>();
        for(Feature feature : features) {
            if(feature.geometry.type.equals("Point")) {
                events.add(feature.toEventEntity());
            }
        }
        return events;
    }
}
