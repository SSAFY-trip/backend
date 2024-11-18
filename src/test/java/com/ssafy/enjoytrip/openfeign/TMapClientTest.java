package com.ssafy.enjoytrip.openfeign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.enjoytrip.openfeign.dto.TMapPlaceSearchResponseDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationRequestDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationResponseDto;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TMapClientTest {
    @Autowired
    TMapClient tMapClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test Route Optimization API")
    void testRouteOptimizationAPI() throws Exception {
        // Given
        List<TMapRouteOptimizationRequestDto.ViaPoint> viaPoints = new ArrayList<>(
                Arrays.asList(
                        TMapRouteOptimizationRequestDto.ViaPoint.builder()
                                .viaPointId("event id 1")
                                .viaPointName("2872491")
                                .viaX("128.89278644")
                                .viaY("37.75449601")
                                .viaPoiId("2872491")
                                .build(),
                        TMapRouteOptimizationRequestDto.ViaPoint.builder()
                                .viaPointId("event id 2")
                                .viaPointName("2372901")
                                .viaX("128.89267524")
                                .viaY("37.75746789")
                                .viaPoiId("2372901")
                                .build(),
                        TMapRouteOptimizationRequestDto.ViaPoint.builder()
                                .viaPointId("event id 3")
                                .viaPointName("2803475")
                                .viaX("128.87892606")
                                .viaY("37.77104938")
                                .viaPoiId("2803475")
                                .build()
                )
        );

        TMapRouteOptimizationRequestDto requestDto = TMapRouteOptimizationRequestDto.builder()
                .startName("4514668")
                .startX("128.90700701")
                .startY("37.76632825")
                .endName("1234613")
                .endX("128.89298074")
                .endY("37.75838445")
                .viaPoints(viaPoints)
                .build();

        // When
        TMapRouteOptimizationResponseDto responseDto = tMapClient.getRouteOptimization20(requestDto);

        // Then
        printDtoAsJson(responseDto);
    }

    @Test
    @DisplayName("Test Place Search")
    void testPlaceSearch() throws Exception {
        // Given
        String searchKeyword = "강릉시 식당";
        Double centerLon = 128.8758;
        Double centerLat = 37.75218;

        // When
        TMapPlaceSearchResponseDto dto = tMapClient.getPlaceSearch(
                searchKeyword,
                null,
                null,
                null,
                centerLon,
                centerLat,
                1
        );

        // Then
        printDtoAsJson(dto);

        System.out.println(dto.toPlaceIdList());
    }

    void printDtoAsJson(Object dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(dto);
        System.out.println(json);
    }
}
