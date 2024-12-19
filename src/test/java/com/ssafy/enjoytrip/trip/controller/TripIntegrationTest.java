package com.ssafy.enjoytrip.trip.controller;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ssafy.enjoytrip.event.mapper.EventMapper;
import com.ssafy.enjoytrip.global.util.S3Util;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.mapper.TripMapper;

import static com.ssafy.enjoytrip.TestUtil.createMultiPartRequest;
import static com.ssafy.enjoytrip.TestUtil.printResponse;
import static com.ssafy.enjoytrip.trip.service.TripServiceImpl.tripImageBasePath;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser
public class TripIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private S3Util s3Util;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private TripMapper tripMapper;

    @AfterEach
    public void tearDown() {
        s3Util.deleteDirectory(tripImageBasePath);
        tripMapper.deleteAllTrips();
        tripMapper.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test s3 image upload")
    void testS3ImageUpload() throws Exception {
        // Given
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "image content".getBytes());

        TripCreateDto tripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .image(mockImageFile)
                .isPublic(true)
                .build();


        // When
        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/trips");
        ResultActions resultActions = mockMvc.perform(createMultiPartRequest(multipartRequest, tripCreateDto).with(csrf()));

        // Then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test GET /trips/{id}")
    void testGetTripById() throws Exception {
        // Given
        // trip created
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "image content".getBytes());

        TripCreateDto tripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .image(mockImageFile)
                .isPublic(true)
                .build();

        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/trips");
        mockMvc.perform(createMultiPartRequest(multipartRequest, tripCreateDto).with(csrf()));

        // When
        ResultActions resultActions = mockMvc.perform(get("/trips/1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Paris"))
                .andDo(result -> printResponse(result));
    }

    @Test
    @DisplayName("Test DELETE /trips/{id}")
    void testDeleteTripById() throws Exception {
        // Given
        AtomicReference<String> createdUid = new AtomicReference<>(null);

        // trip created
        MockMultipartFile mockImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpeg", "image content".getBytes());

        TripCreateDto tripCreateDto = TripCreateDto.builder()
                .name("Paris")
                .startDate(LocalDate.of(2023, 1, 1))
                .endDate(LocalDate.of(2023, 1, 10))
                .tripOverview("A wonderful trip to Paris.")
                .image(mockImageFile)
                .isPublic(true)
                .build();

        MockMultipartHttpServletRequestBuilder multipartRequest = multipart("/trips");
        mockMvc.perform(createMultiPartRequest(multipartRequest, tripCreateDto).with(csrf()));

        // Get created trip
        ResultActions getResultActions = mockMvc.perform(get("/trips/1")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON));

        getResultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Paris"))
                .andDo(result -> {
                    printResponse(result);

                    String responseContent = result.getResponse().getContentAsString();
                    createdUid.set(JsonPath.read(responseContent, "$.uid"));
                });

        assertTrue(s3Util.checkIfFileExists(tripImageBasePath + createdUid.get()));

        // When
        ResultActions deleteResultActions = mockMvc.perform(delete("/trips/" + 1)
                .with(csrf()));

        // Then
        deleteResultActions.andExpect(status().isNoContent());
        assertFalse(s3Util.checkIfFileExists(tripImageBasePath + createdUid.get()));
    }
}
