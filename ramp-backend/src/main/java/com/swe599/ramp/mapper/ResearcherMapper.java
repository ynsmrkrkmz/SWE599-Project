package com.swe599.ramp.mapper;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.model.Researcher;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ResearcherMapper {

    ResearcherMapper INSTANCE = Mappers.getMapper(ResearcherMapper.class);

    Researcher toEntity(ResearcherCreateRequestDto researcherCreateRequestDto);
}
