package com.swe599.ramp.service;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.mapper.ResearcherMapper;
import com.swe599.ramp.repository.ResearcherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResearcherService {

    private final ResearcherRepository researcherRepository;
    private final ResearcherMapper researcherMapper;

    public      void create(ResearcherCreateRequestDto researcherCreateRequestDto) {

        if (researcherRepository.findByOpenAlexId(researcherCreateRequestDto.getOpenAlexId())
            .isEmpty()) {
            researcherRepository.save(researcherMapper.toEntity(researcherCreateRequestDto));
        }
    }
}
