package com.swe599.ramp.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.Stat;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StatMapper {

    StatMapper INSTANCE = Mappers.getMapper(StatMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "citationCountPerYear", target = "citationPerYear", qualifiedByName = "mapToString")
    @Mapping(target = "researcher", source = "researcher")
    Stat toEntity(Researcher researcher,
        Map<Integer, Integer> citationCountPerYear, OffsetDateTime dataDate);

    @Mapping(source = "citationPerYear", target = "citationCountPerYear", qualifiedByName = "stringToMap")
    @Mapping(target = "dataDate", source = "dataDate")
    @Mapping(target = "workCountInTopJournals", source = "workCountInTopJournals")
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

    @Named("stringToMap")
    public static Map<Integer, Integer> stringToMap(String citationPerYear) {
        if (citationPerYear == null || citationPerYear.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return new ObjectMapper().readValue(citationPerYear, new TypeReference<Map<Integer, Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing citationPerYear", e);
        }
    }
}
