package com.swe599.ramp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.swe599.ramp.dto.researcher.ResearcherDto;
import com.swe599.ramp.mapper.OpenAlexMapper;
import com.swe599.ramp.repository.JournalRepository;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class OpenAlexService {

    private final WebClient webClient;
    private final OpenAlexMapper openAlexMapper;
    private final JournalRepository journalRepository;

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
            Map<Integer, Integer> currentPageData = openAlexMapper.mapJsonToCitationPerYear(
                response);
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

    public Flux<Map.Entry<String, Map<Integer, Integer>>> citationPerYearForAllAuthors(
        List<String> authorIds) {
        // For each author, get a Mono of its citation map, then attach the authorId
        return Flux.fromIterable(authorIds)
            .delayElements(Duration.ofMillis(100))
            .flatMap(authorId ->
                citationPerYearByAuthorReactive(authorId)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                    .map(citations -> Map.entry(authorId, citations))
                    .onErrorResume(e -> {
                        System.err.println(
                            "Failed to fetch citations for author: " + authorId + ". Error: "
                                + e.getMessage());
                        return Mono.empty(); // Skip this author on error
                    })
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

    public Flux<Map.Entry<String, Map<Integer, Integer>>> fetchCitationsForAuthors(
        List<String> authorIds) {
        return Flux.fromIterable(authorIds)
            .delayElements(Duration.ofMillis(1000))
            .flatMap(authorId ->
                fetchAndMapCitationsByAuthor(authorId)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                    .map(citations -> Map.entry(authorId, citations))
            )
            .onErrorContinue((error, obj) -> {
                System.err.println(
                    "Error processing author: " + obj + ". Error: " + error.getMessage());
            });
    }

    public Mono<Map<Integer, Integer>> fetchAndMapCitationsByAuthor(String authorId) {
        String baseUrl = "/people/" + authorId;
        return webClient.get()
            .uri(baseUrl)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(openAlexMapper::mapJsonToCitationPerYearByAuthor);
    }

    public Flux<Map.Entry<String, Long>> countWorksInJournalTableForAuthors(List<String> authorIds) {
        return Flux.fromIterable(authorIds) // Process each author ID
            .delayElements(Duration.ofMillis(1000))
            .flatMap(authorId -> countWorksInJournalTableReactive(authorId) // Call the method for each author
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
                .map(workCount -> Map.entry(authorId, workCount)) // Map the result to a Map.Entry
            );
    }

    public Mono<Long> countWorksInJournalTableReactive(String authorId) {
        int perPage = 50;
        String baseUrl = "/works?filter=author.id:" + authorId + "&per_page=" + perPage;

        return webClient.get()
            .uri(baseUrl + "&page=1")
            .retrieve()
            .bodyToMono(JsonNode.class) // Get the first response as Mono<JsonNode>
            .flatMap(firstResponse -> {
                // Determine total pages from the first response
                int totalCount = firstResponse.get("meta").get("count").asInt();
                int totalPages = (totalCount + perPage - 1) / perPage;

                // Fetch all pages and process works
                return Flux.range(1, totalPages) // Generate page numbers
                    .flatMap(pageNumber -> fetchWorksFromPage(baseUrl,
                        pageNumber)) // Fetch works for each page
                    .filter(this::isWorkMatchedInJournalTable) // Check if the work has at least one matched ISSN
                    .count();// Count the number of matches
            });
    }

    private Mono<List<String>> fetchWorksFromPage(String baseUrl, int pageNumber) {
        return webClient.get()
            .uri(baseUrl + "&page=" + pageNumber)
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(openAlexMapper::getIssnsFromWork); // Get "results" array
    }

    // Check if a work has at least one ISSN that exists in the journal table
    public boolean isWorkMatchedInJournalTable(List<String> issns) {
        for (String issn : issns) {
            if (journalRepository.existsByIssn(issn) || journalRepository.existsByEissn(issn)) {
                return true; // At least one match found
            }
        }
        return false; // No matches
    }
}

