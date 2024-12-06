package com.swe599.ramp.dto.researcher;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearcherDto {

    private String id;

    private String openAlexId;

    private String orcId;

    private String name;

    private String institution;

    private String institutionCountry;
}
