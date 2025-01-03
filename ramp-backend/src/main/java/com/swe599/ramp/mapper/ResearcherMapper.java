package com.swe599.ramp.mapper;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.dto.researcher.ResearcherDto;
import com.swe599.ramp.dto.researcher.ResearcherListCreateRequestDto;
import com.swe599.ramp.dto.researcher.ResearcherListDetailDto;
import com.swe599.ramp.dto.researcher.ResearcherListDto;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.ResearcherList;
import com.swe599.ramp.model.User;
import java.util.List;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResearcherMapper {

    ResearcherMapper INSTANCE = Mappers.getMapper(ResearcherMapper.class);

    Researcher toEntity(ResearcherCreateRequestDto researcherCreateRequestDto);

    ResearcherDto toDto(Researcher researcher);

    ResearcherStatsDto toResearcherStatsDto(Map<Integer, Integer> citationCountPerYear);

    ResearcherListDto toResearcherListDto(ResearcherList researcherList);

    @Mapping(target = "name", source = "requestDto.name")
    @Mapping(target = "createdBy", source = "createdBy")
    ResearcherList toResearcherListEntity(ResearcherListCreateRequestDto requestDto, User createdBy);

    @Mapping(target = "id", source = "researcherList.id")
    @Mapping(target = "name", source = "researcherList.name")
    @Mapping(target = "createdBy", source = "researcherList.createdBy")
    @Mapping(target = "researchers", source = "researchers")
    ResearcherListDetailDto toResearcherListDetailDto(ResearcherList researcherList, List<Researcher> researchers );
}
