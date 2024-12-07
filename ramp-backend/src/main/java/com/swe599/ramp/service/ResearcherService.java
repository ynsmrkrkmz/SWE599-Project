package com.swe599.ramp.service;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.mapper.ResearcherMapper;
import com.swe599.ramp.mapper.StatMapper;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.repository.ResearcherRepository;
import com.swe599.ramp.repository.StatRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResearcherService {

    private final ResearcherRepository researcherRepository;
    private final StatRepository statRepository;
    private final ResearcherMapper researcherMapper;
    private final StatMapper statMapper;
    private final OpenAlexService openAlexService;

    @Transactional
    public void create(ResearcherCreateRequestDto researcherCreateRequestDto) {

        if (researcherRepository.findByOpenAlexId(researcherCreateRequestDto.getOpenAlexId())
            .isEmpty()) {

            Researcher created = researcherRepository.save(
                researcherMapper.toEntity(researcherCreateRequestDto));

            statRepository.save(statMapper.toEntity(created,
                openAlexService.citationPerYearByAuthor(
                    researcherCreateRequestDto.getOpenAlexId())));
        }
    }

    public ResearcherStatsDto getResearcherStats(String authorId) {
        // TODO: Will be fetched from DB
        Map<Integer, Integer> citationCountPerYear = openAlexService.citationPerYearByAuthor(
            authorId);

        // Use MapStruct to map and combine the data
        return researcherMapper.toResearcherStatsDto(citationCountPerYear);
    }
}
