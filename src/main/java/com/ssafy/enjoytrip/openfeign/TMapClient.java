package com.ssafy.enjoytrip.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.enjoytrip.openfeign.config.TMapClientConfig;
import com.ssafy.enjoytrip.openfeign.dto.TMapPlaceDetailResponseDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapPlaceSearchResponseDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationRequestDto;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationResponseDto;

@FeignClient(name = "TMapClient",
        url = "https://apis.openapi.sk.com",
        configuration = TMapClientConfig.class
)
public interface TMapClient {
    @GetMapping("/tmap/pois/{placeId}")
    TMapPlaceDetailResponseDto getPlaceDetails(@PathVariable("placeId") String placeId);

    @GetMapping("/tmap/routes/routeOptimization20?version=1")
    TMapRouteOptimizationResponseDto getRouteOptimization20(@RequestBody TMapRouteOptimizationRequestDto dto);

    @GetMapping("/tmap/pois?version=1")
    TMapPlaceSearchResponseDto getPlaceSearch(
            @RequestParam String searchKeyword,
            @RequestParam(required = false) Integer areaLLCode,
            @RequestParam(required = false) Integer areaLMCode,
            @RequestParam(required = false) Integer radius,
            @RequestParam(required = false) Double centerLon,
            @RequestParam(required = false) Double centerLat,
            @RequestParam(required = false, defaultValue = "1") Integer page
    );
}
