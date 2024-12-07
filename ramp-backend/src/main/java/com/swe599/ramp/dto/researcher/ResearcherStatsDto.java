package com.swe599.ramp.dto.researcher;

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

    Map<Integer, Integer> citationCountPerYear;
}
