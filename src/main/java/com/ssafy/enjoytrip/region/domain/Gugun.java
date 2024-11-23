package com.ssafy.enjoytrip.region.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Gugun {

    private Integer no;
    private Integer sidoCode;

    @NotBlank
    @NotNull
    private Integer gugunCode;

    @NotBlank
    @NotNull
    private String gugunName;

    @Builder
    public Gugun(Integer no, Integer sidoCode, Integer gugunCode, String gugunName) {
        this.no = no;
        this.sidoCode = sidoCode;
        this.gugunCode = gugunCode;
        this.gugunName = gugunName;
    }
}
