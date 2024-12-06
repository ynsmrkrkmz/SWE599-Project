package com.swe599.ramp.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.swe599.ramp.dto.researcher.ResearcherDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OpenAlexMapper {

    public List<ResearcherDto> mapJsonToResearcherDto(JsonNode jsonResponse) {
        List<ResearcherDto> researcherDto = new ArrayList<>();

        // Check if "results" exists in the response
        if (jsonResponse.has("results")) {
            for (JsonNode result : jsonResponse.get("results")) {
                ResearcherDto dto = new ResearcherDto();

                // Map fields
                dto.setOpenAlexId(extractOpenAlexId(result.get("id").asText()));
                dto.setOrcId(extractOrcId(result.get("orcid").asText()));
                dto.setName(result.get("display_name").asText()); // Map "display_name" to "name"

                // Set institution and country as null (not available in this JSON)
                for (JsonNode institution : result.get("last_known_institutions")) {
                    dto.setInstitution(institution.get("display_name").asText());
                    dto.setInstitutionCountry(institution.get("country_code").asText());
                }

                // Add to the list
                researcherDto.add(dto);
            }
        }

        return researcherDto;
    }

    private String extractOpenAlexId(String id) {
        if (id != null && id.startsWith("https://openalex.org/")) {
            String[] parts = id.split("/");
            return parts[parts.length - 1];
        }
        return null;
    }

    private String extractOrcId(String id) {
        if (id != null && id.startsWith("https://orcid.org/")) {
            String[] parts = id.split("/");
            return parts[parts.length - 1];
        }
        return null;
    }
}
