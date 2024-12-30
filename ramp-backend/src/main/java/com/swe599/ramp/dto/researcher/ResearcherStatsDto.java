package com.swe599.ramp.dto.researcher;

import java.time.OffsetDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearcherStatsDto {

    private ResearcherDto researcher;

    private Map<Integer, Integer> citationCountPerYear;

    private Long workCountInTopJournals;

    private OffsetDateTime dataDate;
}
