package com.swe599.ramp.dto.researcher;

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
public class ResearcherListDetailDto {

    private Long id;

    private String name;

    private UserDto createdBy;

    private List<ResearcherDto> researchers;
}
