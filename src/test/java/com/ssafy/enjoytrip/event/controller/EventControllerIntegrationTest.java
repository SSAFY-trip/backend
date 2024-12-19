package com.ssafy.enjoytrip.event.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.ssafy.enjoytrip.trip.mapper.TripMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ssafy.enjoytrip.event.domain.Event;
import com.ssafy.enjoytrip.event.dto.EventUpdateOrderDto;
import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.event.dto.RouteOptimizationRequestDto;
import com.ssafy.enjoytrip.trip.domain.Trip;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
public class EventControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TripMapper tripMapper;

    // Fixtures
    private Trip trip;
    private Event event1;
    private Event event2;
    private Event event3;
    private Event event4;
    private Event event5;

    @BeforeEach
    void setUp() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        trip = Trip.builder()
                .uid("test")
                .name("강릉 여행")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .tripOverview("A short trip to explore Gangneung's beautiful landscapes.")
                .isPublic(true)
                .build();

        tripMapper.insertTrip(trip);

        event1 = Event.builder()
                .placeId("4514668")
                .name("엄지네포장마차 본점")
                .date(LocalDate.now())
                .latitude(37.76632825)
                .longitude(128.90700701)
                .category("한식")
                .build();

        event2 = Event.builder()
                .placeId("1234613")
                .date(LocalDate.now())
                .name("원조강릉교동반점 본점 [중식]")
                .category("중식")
                .latitude(37.75838445)
                .longitude(128.89298074)
                .build();

        event3 = Event.builder()
                .placeId("2872491")
                .date(LocalDate.now().plusDays(1))
                .name("벌집")
                .category("한식")
                .latitude(37.75449601)
                .longitude(128.89278644)
                .build();

        event4 = Event.builder()
                .placeId("2372901")
                .date(LocalDate.now().plusDays(1))
                .name("현대장칼국수")
                .category("한식")
                .latitude(37.75746789)
                .longitude(128.89267524)
                .build();

        event5 = Event.builder()
                .placeId("2803475")
                .date(LocalDate.now().plusDays(4))
                .name("해미가")
                .category("한식")
                .latitude(37.77104938)
                .longitude(128.87892606)
                .build();

        eventMapper.insertEvent(trip.getId(), LocalDate.now(), event1);
        eventMapper.insertEvent(trip.getId(), LocalDate.now(), event2);
        eventMapper.insertEvent(trip.getId(), LocalDate.now(), event3);
        eventMapper.insertEvent(trip.getId(), LocalDate.now(), event4);
        eventMapper.insertEvent(trip.getId(), LocalDate.now(), event5);
    }

    @AfterEach
    void tearDown() {
        eventMapper.deleteAllEvents();
        eventMapper.resetAutoIncrement();
        tripMapper.deleteAllTrips();
        tripMapper.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test Get Ordered Events Of Trip Id")
    void testGetOrderedEventOfTripId() throws Exception {
        mockMvc.perform(get("/trips/{tripId}/events", trip.getId()))
                .andExpect(status().isOk())
                .andDo(result -> {
                    printResponse(result);
                });
    }

    @Test
    @DisplayName("Test Optimize Route Of Event")
    void testOptimizeRouteOfEvent() throws Exception {
        // Given
        RouteOptimizationRequestDto requestDto = RouteOptimizationRequestDto.builder()
                .startEventId(event1.getId())
                .startEventLatitude(event1.getLatitude())
                .startEventLongitude(event1.getLongitude())
                .endEventId(event2.getId())
                .endEventLatitude(event2.getLatitude())
                .endEventLongitude(event2.getLongitude())
                .build();

        // When & Then
        mockMvc.perform(post("/trips/{tripId}/events/by-date/{date}/optimize", trip.getId(), LocalDate.now())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    printResponse(result);
                });

    }

    @Test
    @DisplayName("Test Get Place Details Of All Events")
    void testGetPlaceDetailsOfAllEvents() throws Exception {
        // When & Then
        mockMvc.perform(get("/trips/{tripId}/events/details", trip.getId())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.size()").value(5))
                .andDo(result -> {
                    printResponse(result);
                });
    }

    @Test
    @DisplayName("Test Get Place Details Of Search")
    void testGetPlaceDetailsOfSearch() throws Exception {
        // Given
        int tripId = 1;
        EventUpdateOrderDto.SearchRequestDto dto = EventUpdateOrderDto.SearchRequestDto.builder()
                .query("강릉시 식당")
                .centerLon(128.8758)
                .centerLat(37.75218)
                .build();

        long startTime = System.nanoTime();

        // When & Then
        mockMvc.perform(post("/trips/{tripId}/events/search", tripId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(result -> {
                    // print time
                    long endTime = System.nanoTime();
                    long durationInMillis = (endTime - startTime) / 1_000_000;
                    System.out.println("Request and response time: " + durationInMillis + " ms");

                    printResponse(result);
                });
    }

    void printDtoAsJson(Object dto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(dto);
        System.out.println(json);
    }

    void printResponse(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        Object json = objectMapper.readValue(jsonResponse, Object.class); // Deserialize
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); // Pretty print

        System.out.println(prettyJson);
    }
}
