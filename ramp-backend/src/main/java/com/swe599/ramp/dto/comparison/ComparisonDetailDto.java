package com.swe599.ramp.dto.comparison;

import com.swe599.ramp.dto.researcher.ResearcherDto;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.dto.user.UserDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonDetailDto {

    private Long id;

    private String name;

    private ComparisonAnalysisDto list1Analysis;

    private ComparisonAnalysisDto list2Analysis;
}
