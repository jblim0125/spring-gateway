package com.jblim.gateway.models.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class StringToMapConverter implements Converter<String, Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> convert(String source) {
        try {
            if (source.isEmpty()) {
                return new HashMap<>();
            }
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {
            };
            return objectMapper.readValue(source, typeRef);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to Map", e);
        }
    }
}

