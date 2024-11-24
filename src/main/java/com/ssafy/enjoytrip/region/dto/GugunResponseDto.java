package com.ssafy.enjoytrip.region.dto;

import com.ssafy.enjoytrip.region.domain.Gugun;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GugunResponseDto {
    private Integer no;
    private Integer sidoCode;
    private Integer gugunCode;
    private String gugunName;

    public static GugunResponseDto of(Gugun gugun) {
        return GugunResponseDto.builder()
                .no(gugun.getNo())
                .sidoCode(gugun.getSidoCode())
                .gugunCode(gugun.getGugunCode())
                .gugunName(gugun.getGugunName())
                .build();
    }
}
