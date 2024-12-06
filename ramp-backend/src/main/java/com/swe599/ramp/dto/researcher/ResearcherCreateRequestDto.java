package com.swe599.ramp.dto.researcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearcherCreateRequestDto {

    private String openAlexId;

    private String orcId;

    private String name;

    private String institution;

    private String institutionCountry;
}
