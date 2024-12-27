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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    // Returns a Mono instead of a Map
    public Mono<Map<Integer, Integer>> citationPerYearByAuthorReactive(String authorId) {
        int perPage = 50;
        String baseUrl = "/works?filter=author.id:" + authorId + "&per_page=" + perPage;

        // First call to determine total pages
        return webClient.get()
            .uri(baseUrl + "&page=1")
            .retrieve()
            .bodyToMono(JsonNode.class)
            .flatMap(firstResponse -> {
                // Use firstResponse to figure out totalPages
                int totalCount = firstResponse.get("meta").get("count").asInt();
                int totalPages = (totalCount + perPage - 1) / perPage;

                // Start a Flux of page numbers from 1 to totalPages
                return Flux.range(1, totalPages)
                    .flatMap(pageNumber -> fetchAndMapCitations(baseUrl, pageNumber))
                    .reduce(new TreeMap<Integer, Integer>(), (acc, currentMap) -> {
                        // Merge each page’s map into the accumulator
                        currentMap.forEach((year, count) ->
                            acc.merge(year, count, Integer::sum)
                        );
                        return acc;
                    });
            });
    }

    public Flux<Map.Entry<String, Map<Integer, Integer>>> citationPerYearForAllAuthors(List<String> authorIds) {
        // For each author, get a Mono of its citation map, then attach the authorId
        return Flux.fromIterable(authorIds)
            .flatMap(authorId ->
                citationPerYearByAuthorReactive(authorId)
                    .map(citations -> Map.entry(authorId, citations))
            );
    }

    private Mono<Map<Integer, Integer>> fetchAndMapCitations(String baseUrl, int pageNumber) {
        String pageUrl = baseUrl + "&page=" + pageNumber;
        return webClient.get()
            .uri(pageUrl)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(openAlexMapper::mapJsonToCitationPerYear);
    }
}

