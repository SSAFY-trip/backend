package com.ssafy.enjoytrip.event.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.enjoytrip.global.exception.GlobalErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.event.dto.EventCreateDto;
import com.ssafy.enjoytrip.event.dto.EventResponseDto;
import com.ssafy.enjoytrip.event.dto.EventUpdateMemoDto;
import com.ssafy.enjoytrip.event.dto.EventUpdateOrderDto;
import com.ssafy.enjoytrip.global.exception.GlobalExceptionHandler;
import com.ssafy.enjoytrip.event.service.EventService;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(EventController.class)
@WithMockUser
@ContextConfiguration(classes = {EventController.class, GlobalExceptionHandler.class})
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventService eventService;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("Test Create Event - with valid data")
    void testCreateEvent() throws Exception {
        // Given
        int tripId = 1;
        EventCreateDto eventCreateDto = EventCreateDto.builder()
                .name("Test")
                .date(LocalDate.now())
                .memo("memo")
                .latitude(124.56)
                .longitude(124.56)
                .category("category")
                .build();

        // When
        mockMvc.perform(post("/trips/" + tripId + "/events")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventCreateDto)))
                .andExpect(status().isCreated());

        // Then
        verify(eventService, times(1)).createEvent(eq(tripId), refEq(eventCreateDto));
    }

    @Test
    void name() {

    }

    @Test
    @DisplayName("Test Create Event - with invalid data")
    void testCreateEventWithInvalidData() throws Exception {
        // Given
        int tripId = 1;
        EventCreateDto eventCreateDto = EventCreateDto.builder().build();

        // When
        ResultActions resultActions = mockMvc.perform(post("/trips/" + tripId + "/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventCreateDto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(
                        GlobalErrorCode.VALIDATION_FAILED.getCode(),
                        getCode(result),
                        "Expected error code does not match."))
                .andDo(result -> printResponse(result));

        verify(eventService, never()).createEvent(anyInt(), any());
    }

    @Test
    @DisplayName("Test Get Ordered Events By Trip Id")
    void testGetOrderedEventsByTripId() throws Exception {
        // Given
        int tripId = 1;
        Event event1 = Event.builder().name("Event 1").order(1).date(LocalDate.now()).build();
        Event event2 = Event.builder().name("Event 2").order(2).date(LocalDate.now()).build();
        Event event3 = Event.builder().name("Event 2").order(1).date(LocalDate.now().plusDays(1)).build();

        Map<String, List<EventResponseDto>> events = new TreeMap<>();
        events.put(LocalDate.now().toString(),
                Arrays.asList(
                        EventResponseDto.of(event1),
                        EventResponseDto.of(event2)
                )
        );
        events.put(LocalDate.now().plusDays(1).toString(),
                Arrays.asList(
                        EventResponseDto.of(event3)
                )
        );

        when(eventService.getOrderedEventsByTripId(tripId)).thenReturn((TreeMap<String, List<EventResponseDto>>) events);

        // When
        ResultActions resultActions = mockMvc.perform(get("/trips/{tripId}/events", tripId));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey(LocalDate.now().toString())))
                .andExpect(jsonPath("$", hasKey(LocalDate.now().plusDays(1).toString())));

        verify(eventService, times(1)).getOrderedEventsByTripId(eq(tripId));
    }


    @Test
    @DisplayName("Test Update Event Memo")
    void testUpdateEventMemo() throws Exception {
        // Given
        int eventId = 1;
        EventUpdateMemoDto eventUpdateMemoDto = EventUpdateMemoDto.builder()
                .memo("Updated memo")
                .build();

        // When
        mockMvc.perform(patch("/trips/{tripId}/events/{eventId}", 1, eventId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventUpdateMemoDto)))
                .andExpect(status().isNoContent());

        // Then
        verify(eventService, times(1)).updateEventMemo(eq(eventId), refEq(eventUpdateMemoDto));
    }

    @Test
    @DisplayName("Test Update Event Memo - with invalid data")
    void testUpdateEventMemoWithInvalidData() throws Exception {
        // Given
        int eventId = 1;
        String invalidMemo = "a".repeat(301);
        EventUpdateMemoDto eventUpdateMemoDto = EventUpdateMemoDto.builder()
                .memo(invalidMemo)
                .build();

        // When
        ResultActions resultActions = mockMvc.perform(patch("/trips/{tripId}/events/{eventId}", 1, eventId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventUpdateMemoDto)));

        // Then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertEquals(
                        GlobalErrorCode.VALIDATION_FAILED.getCode(),
                        getCode(result),
                        "Expected error code does not match."))
                .andDo(result -> printResponse(result));
        verify(eventService, never()).updateEventMemo(anyInt(), any());
    }


    @Test
    @DisplayName("Test Update Order Of Events")
    void testUpdateOrderOfEvents() throws Exception {
        // Given
        int tripId = 1;
        LocalDate date = LocalDate.now();

        List<EventUpdateOrderDto> eventUpdateOrderDtos = Arrays.asList(
                EventUpdateOrderDto.builder().id(1).build(),
                EventUpdateOrderDto.builder().id(2).build()
        );

        // When
        mockMvc.perform(patch("/trips/{tripId}/events/by-date/{date}", tripId, date.toString())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventUpdateOrderDtos)))
                .andExpect(status().isNoContent());

        // Then
        verify(eventService, times(1)).updateOrderOfEvents(eq(tripId), eq(date), anyList());
    }

    @Test
    @DisplayName("Test Delete Event")
    void testDeleteEvent() throws Exception {
        // Given
        int tripId = 1;
        int eventId = 1;

        // When
        mockMvc.perform(delete("/trips/{tripId}/events/{eventId}", tripId, eventId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        // Then
        verify(eventService, times(1)).deleteEvent(eq(tripId), eq(eventId));  // Correct method call with only eventId
    }

    /*
    utils
     */
    void printResponse(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        Object json = objectMapper.readValue(jsonResponse, Object.class); // Deserialize
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); // Pretty print

        System.out.println(prettyJson);
    }

    String getCode(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("code").asText();
    }
}
