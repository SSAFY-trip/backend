package com.ssafy.enjoytrip.openfeign.deserializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.enjoytrip.openfeign.dto.TMapRouteOptimizationResponseDto;

public class GeometryDeserializer extends JsonDeserializer<Object> {
    @Override
    public TMapRouteOptimizationResponseDto.Feature.Geometry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        ObjectMapper mapper = (ObjectMapper) p.getCodec();

        String type = node.has("type") ? node.get("type").asText() : null;
        JsonNode coordinatesNode = node.has("coordinates") ? node.get("coordinates") : null;

        Object coordinates = null;

        if (coordinatesNode != null) {
            if ("Point".equals(type)) {
                coordinates = mapper.convertValue(coordinatesNode, new TypeReference<List<Double>>(){});
            } else if ("LineString".equals(type)) {
                coordinates = mapper.convertValue(coordinatesNode, new TypeReference<List<List<Double>>>(){});
            }
        }

        return new TMapRouteOptimizationResponseDto.Feature.Geometry(type, coordinates);
    }
}
