package com.swe599.ramp.dto.comparison;

import com.swe599.ramp.dto.researcher.ResearcherListDto;
import com.swe599.ramp.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonCreateRequestDto {

    private String name;

    private Long list1;

    private Long list2;
}
