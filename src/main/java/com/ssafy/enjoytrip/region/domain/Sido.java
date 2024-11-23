package com.ssafy.enjoytrip.region.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class Sido {

    private Integer no;

    @NotBlank
    @NotNull
    private Integer sidoCode;

    @NotBlank
    @NotNull
    private String sidoName;

    @Builder
    public Sido(Integer no, Integer sidoCode, String sidoName) {
        this.no = no;
        this.sidoCode = sidoCode;
        this.sidoName = sidoName;
    }
}
