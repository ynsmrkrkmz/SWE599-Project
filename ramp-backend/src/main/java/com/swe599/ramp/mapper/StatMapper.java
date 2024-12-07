package com.swe599.ramp.mapper;

import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.Stat;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    Stat toEntity(Researcher researcher,
        Map<Integer, Integer> citationCountPerYear);

    ResearcherStatsDto toResearcherStatDto(Stat stat);
}
