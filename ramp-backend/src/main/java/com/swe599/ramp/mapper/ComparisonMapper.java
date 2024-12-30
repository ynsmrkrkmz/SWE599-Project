package com.swe599.ramp.mapper;

import com.swe599.ramp.dto.comparison.ComparisonDto;
import com.swe599.ramp.model.Comparison;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ComparisonMapper {

    ComparisonMapper INSTANCE = Mappers.getMapper(ComparisonMapper.class);

    ComparisonDto toDto(Comparison comparison);
}
