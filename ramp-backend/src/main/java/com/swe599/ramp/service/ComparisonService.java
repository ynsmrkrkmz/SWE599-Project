package com.swe599.ramp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe599.ramp.dto.comparison.ComparisonAnalysisDto;
import com.swe599.ramp.dto.comparison.ComparisonCreateRequestDto;
import com.swe599.ramp.dto.comparison.ComparisonDetailDto;
import com.swe599.ramp.dto.comparison.ComparisonDto;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.mapper.ComparisonMapper;
import com.swe599.ramp.mapper.ResearcherMapper;
import com.swe599.ramp.mapper.StatMapper;
import com.swe599.ramp.model.Comparison;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.ResearcherList;
import com.swe599.ramp.model.ResearcherListMembership;
import com.swe599.ramp.model.Stat;
import com.swe599.ramp.model.User;
import com.swe599.ramp.repository.ComparisonRepository;
import com.swe599.ramp.repository.ResearcherListMembershipRepository;
import com.swe599.ramp.repository.ResearcherListRepository;
import com.swe599.ramp.repository.StatRepository;
import com.swe599.ramp.utils.AnalysisUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ComparisonService {

    private final ComparisonRepository comparisonRepository;
    private final ComparisonMapper comparisonMapper;
    private final ResearcherListRepository researcherListRepository;
    private final ResearcherListMembershipRepository researcherListMembershipRepository;
    private final StatRepository statRepository;
    private final ResearcherMapper researcherMapper;
    private final StatMapper statMapper;
    private final ResearcherService researcherService;

    @Transactional
    public ComparisonDto addComparison(ComparisonCreateRequestDto requestDto,
        User createdBy) {

        if (comparisonRepository.findByNameAndCreatedById(requestDto.getName(),
                createdBy.getId())
            .isPresent()) {
            throw new RuntimeException(
                String.format("Comparison with name %s is already exist", requestDto.getName()));
        }

        ResearcherList list1 = researcherListRepository.findById(requestDto.getList1())
            .orElseThrow();
        ResearcherList list2 = researcherListRepository.findById(requestDto.getList2())
            .orElseThrow();

        Comparison comparison = new Comparison();
        comparison.setName(requestDto.getName());
        comparison.setList1(list1);
        comparison.setList2(list2);
        comparison.setCreatedBy(createdBy);

        return comparisonMapper.toDto(
            comparisonRepository.save(comparison));
    }

    public List<ComparisonDto> getAllComparisons(Long createdById) {
        return comparisonRepository.findAllByCreatedById(createdById).stream()
            .map(comparisonMapper::toDto).toList();
    }

    @Transactional
    public ComparisonDetailDto getComparisonDetails(Long comparisonId)
        throws JsonProcessingException {

        Comparison comparison = comparisonRepository.findById(comparisonId).orElseThrow();

        // Process List1 and List2
        ComparisonAnalysisDto list1Analysis = processResearcherList(comparison.getList1().getId());
        ComparisonAnalysisDto list2Analysis = processResearcherList(comparison.getList2().getId());

        // Set details in DTO
        ComparisonDetailDto comparisonDetailDto = new ComparisonDetailDto();
        comparisonDetailDto.setId(comparisonId);
        comparisonDetailDto.setName(comparison.getName());
        comparisonDetailDto.setList1Analysis(list1Analysis);
        comparisonDetailDto.setList2Analysis(list2Analysis);

        return comparisonDetailDto;
    }

    private ComparisonAnalysisDto processResearcherList(Long listId)
        throws JsonProcessingException {
        List<ResearcherListMembership> memberships = researcherListMembershipRepository.findAllByResearcherListId(
            listId);

        Map<Long, String> groupStats = new HashMap<>();
        List<ResearcherStatsDto> researcherStats = new ArrayList<>();

        for (ResearcherListMembership membership : memberships) {
            Stat stat = statRepository.findByResearcherId(membership.getResearcher().getId())
                .orElseThrow();

            ResearcherStatsDto researcherStat = statMapper.toResearcherStatDto(stat);
            groupStats.put(membership.getResearcher().getId(), stat.getCitationPerYear());
            researcherStats.add(researcherStat);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        double mean = AnalysisUtils.calculateAveragesFromJson(
            objectMapper.writeValueAsString(groupStats));

        ResearcherList researcherList = researcherListRepository.findById(listId).orElseThrow();

        ComparisonAnalysisDto analysisDto = new ComparisonAnalysisDto();
        analysisDto.setResearcherListName(researcherList.getName());
        analysisDto.setMean(mean);
        analysisDto.setResearcherStats(researcherStats);

        return analysisDto;
    }

    @Transactional
    public void updateResearcherDataByComparisonId(Long comparisonId) {

        Comparison comparison = comparisonRepository.findById(comparisonId).orElseThrow();
        List<Researcher> researchers = new ArrayList<>();

        researchers.addAll(getResearchersInList(comparison.getList1().getId()));
        researchers.addAll(getResearchersInList(comparison.getList2().getId()));

        researcherService.updateResearchersData(researchers);
    }

    private List<Researcher> getResearchersInList(Long listId){
        List<Researcher> researchersInlist = new ArrayList<>();

        List<ResearcherListMembership> memberships = researcherListMembershipRepository.findAllByResearcherListId(
            listId);

        for (ResearcherListMembership membership : memberships) {
            researchersInlist.add(membership.getResearcher());
        }

        return researchersInlist;
    }
}
