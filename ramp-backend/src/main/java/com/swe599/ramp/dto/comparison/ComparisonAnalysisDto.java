package com.swe599.ramp.dto.comparison;

import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonAnalysisDto {

    private String researcherListName;

    private Double mean;

    private List<ResearcherStatsDto> researcherStats;
}
