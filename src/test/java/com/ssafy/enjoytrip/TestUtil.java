package com.ssafy.enjoytrip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.trip.dto.TripCreateDto;
import com.ssafy.enjoytrip.trip.dto.TripUpdateDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static MockHttpServletRequestBuilder createMultiPartRequest(
            MockMultipartHttpServletRequestBuilder multipartRequest,
            TripCreateDto tripCreateDto
    ) throws IllegalAccessException {
        Field[] fields = tripCreateDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();
            Object fieldValue = field.get(tripCreateDto);

            if ("image".equals(fieldName) && fieldValue instanceof MockMultipartFile) {
                multipartRequest.file((MockMultipartFile) fieldValue);
            } else {
                String value = convertFieldToString(field, fieldValue);
                if (!value.isEmpty()) {
                    multipartRequest.param(fieldName, value);
                }
            }
        }
        return multipartRequest;
    }

    public static MockHttpServletRequestBuilder createMultiPartRequest(
            MockMultipartHttpServletRequestBuilder multipartRequest,
            TripUpdateDto tripUpdateDto
    ) throws IllegalAccessException {
        Field[] fields = tripUpdateDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            String fieldName = field.getName();
            Object fieldValue = field.get(tripUpdateDto);

            if ("image".equals(fieldName) && fieldValue instanceof MockMultipartFile) {
                multipartRequest.file((MockMultipartFile) fieldValue);
            } else {
                String value = convertFieldToString(field, fieldValue);
                if (!value.isEmpty()) {
                    multipartRequest.param(fieldName, value);
                }
            }
        }
        return multipartRequest;
    }

    public static String convertFieldToString(Field field, Object fieldValue) {
        if (fieldValue == null) {
            return "";
        }

        if (fieldValue instanceof LocalDate) {
            return ((LocalDate) fieldValue).format(DateTimeFormatter.ISO_DATE);
        }

        return fieldValue.toString();
    }

    public static void printResponse(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonResponse = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        Object json = objectMapper.readValue(jsonResponse, Object.class); // Deserialize
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json); // Pretty print

        System.out.println(prettyJson);
    }

    public static String getCode(MvcResult result) throws UnsupportedEncodingException, JsonProcessingException {
        return objectMapper.readTree(result.getResponse().getContentAsString()).get("code").asText();
    }
}
