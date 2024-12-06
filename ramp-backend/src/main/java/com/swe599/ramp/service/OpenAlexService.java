package com.swe599.ramp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.swe599.ramp.dto.researcher.ResearcherDto;
import com.swe599.ramp.mapper.OpenAlexMapper;
import java.util.List;
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
}

