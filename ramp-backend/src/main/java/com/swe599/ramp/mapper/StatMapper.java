package com.swe599.ramp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.Stat;
import java.time.OffsetDateTime;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    @Mapping(source = "citationCountPerYear", target = "citationPerYear", qualifiedByName = "mapToString")
    Stat toEntity(Researcher researcher,
        Map<Integer, Integer> citationCountPerYear, OffsetDateTime dataDate);

    ResearcherStatsDto toResearcherStatDto(Stat stat);

    @Named("mapToString")
    public static String mapToString(Map<Integer, Integer> citationCountPerYear) {
        if (citationCountPerYear == null) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(citationCountPerYear);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing citationCountPerYear", e);
        }
    }
}
