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
public class ComparisonDto {

    private Long id;

    private String name;

    private ResearcherListDto list1;

    private ResearcherListDto list2;

    private UserDto createdBy;
}
