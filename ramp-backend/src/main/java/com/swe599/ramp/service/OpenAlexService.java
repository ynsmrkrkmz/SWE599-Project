package com.swe599.ramp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.swe599.ramp.dto.researcher.ResearcherDto;
import com.swe599.ramp.mapper.OpenAlexMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class OpenAlexService {

    private final WebClient webClient;
    private final OpenAlexMapper openAlexMapper;

    public List<ResearcherDto> searchResearchers(String query) {
        JsonNode response = webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/authors")
                .queryParam("search", query)
                .queryParam("per_page", 5)
                .build())
            .retrieve()
            .bodyToMono(JsonNode.class)
            .block(); // Blocking call for simplicity; consider asynchronous for production

        assert response != null;
        return openAlexMapper.mapJsonToResearcherDto(response);
    }

    public Map<Integer, Integer> citationPerYearByAuthor(String authorId) {
        int perPage = 50;
        String baseUrl = "/works?filter=author.id:" + authorId + "&per_page=" + perPage;
        Map<Integer, Integer> citationCountPerYear = new TreeMap<>();

        int currentPage = 1; // Start with the first page
        int totalPages = 1;  // Will be updated after the first API call

        do {
            // Build the URL for the current page
            String pageUrl = baseUrl + "&page=" + currentPage;

            JsonNode response = webClient.get()
                .uri(pageUrl)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

            // Combine current page's data into the overall map
            Map<Integer, Integer> currentPageData = openAlexMapper.mapJsonToCitationPerYear(response);
            currentPageData.forEach((year, count) ->
                citationCountPerYear.merge(year, count, Integer::sum)
            );

            // Update totalPages based on meta information
            if (response != null && response.has("meta")) {
                int totalCount = response.get("meta").get("count").asInt();
                totalPages = (totalCount + perPage - 1) / perPage; // Calculate total pages
            }

            currentPage++; // Move to the next page
        } while (currentPage <= totalPages);

        return citationCountPerYear;
    }

    ;
}

