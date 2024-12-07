package com.swe599.ramp.dto.researcher;

import com.swe599.ramp.model.Researcher;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResearcherStatCreateDto {

    private Researcher researcher;

    Map<Integer, Integer> citationCountPerYear;
}
