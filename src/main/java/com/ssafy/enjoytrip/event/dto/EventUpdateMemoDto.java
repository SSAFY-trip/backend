package com.ssafy.enjoytrip.event.dto;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import com.ssafy.enjoytrip.domain.Event;

@Builder
@Getter
public class EventUpdateMemoDto {
    private Integer id;

    @Size(max = 300, message = "Memo must be at most 300 characters")
    private String memo;

    public void setUpdateId(Integer id){
        this.id = id;
    }

    public Event toEntity() {
        return Event.builder()
                .id(id)
                .memo(memo)
                .build();
    }
}
