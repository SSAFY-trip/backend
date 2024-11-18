package com.ssafy.enjoytrip.event.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.ssafy.enjoytrip.dto.EventCreateDto;
import com.ssafy.enjoytrip.dto.EventResponseDto;
import com.ssafy.enjoytrip.dto.EventUpdateMemoDto;
import com.ssafy.enjoytrip.dto.EventUpdateOrderDto;

public interface EventService {
    void createEvent(Integer tripId, EventCreateDto eventDto);

    Map<LocalDate, List<EventResponseDto>> getOrderedEventsByTripId(Integer tripId);

    void updateEventMemo(Integer id, EventUpdateMemoDto eventDto);

    void updateOrderOfEvents(Integer tripId, LocalDate date, List<EventUpdateOrderDto> eventDtos);

    void deleteEvent(Integer tripId, Integer eventId);

}
