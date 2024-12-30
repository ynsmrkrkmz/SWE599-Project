package com.swe599.ramp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AnalysisUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static double calculateAveragesFromJson(String json) {
        try {
            // Parse the outer JSON (Map<String, String>)
            Map<String, String> outerMap = objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});

            List<Double> averages = new ArrayList<>();

            // Loop through each stringified JSON and parse it into a Map<Integer, Integer>
            for (Map.Entry<String, String> entry : outerMap.entrySet()) {
                String researcherId = entry.getKey();
                String innerJson = entry.getValue();

                // Parse the inner JSON
                Map<Integer, Integer> yearData = objectMapper.readValue(innerJson, new TypeReference<Map<Integer, Integer>>() {});

                // Calculate the total citations
                double totalCitations = yearData.values().stream().mapToDouble(Integer::doubleValue).sum();

                // Calculate the active years (from first year to last year)
                int firstYear = Collections.min(yearData.keySet());
                int lastYear = Collections.max(yearData.keySet());
                int activeYears = lastYear - firstYear + 1;

                // Calculate the average based on active years
                double average = totalCitations / activeYears;
                averages.add(average);
            }

            // Return the overall average
            return averages.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON", e);
        }
    }
}


