package com.swe599.ramp.service;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.dto.researcher.ResearcherDto;
import com.swe599.ramp.dto.researcher.ResearcherListCreateRequestDto;
import com.swe599.ramp.dto.researcher.ResearcherListDetailDto;
import com.swe599.ramp.dto.researcher.ResearcherListDto;
import com.swe599.ramp.dto.researcher.ResearcherStatsDto;
import com.swe599.ramp.mapper.ResearcherMapper;
import com.swe599.ramp.mapper.StatMapper;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.ResearcherList;
import com.swe599.ramp.model.ResearcherListMembership;
import com.swe599.ramp.model.ResearcherListMembershipId;
import com.swe599.ramp.model.Stat;
import com.swe599.ramp.model.User;
import com.swe599.ramp.repository.ResearcherListMembershipRepository;
import com.swe599.ramp.repository.ResearcherListRepository;
import com.swe599.ramp.repository.ResearcherRepository;
import com.swe599.ramp.repository.StatRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final ResearcherListMembershipRepository researcherListMembershipRepository;
    private final ResearcherListRepository researcherListRepository;

    @Transactional
    public void addResearcher(ResearcherCreateRequestDto researcherCreateRequestDto) {

        Optional<Researcher> researcher = researcherRepository.findByOpenAlexId(
            researcherCreateRequestDto.getOpenAlexId());

        if (researcher.isEmpty()) {

            Researcher created = researcherRepository.save(
                researcherMapper.toEntity(researcherCreateRequestDto));

            researcher = Optional.of(created);

            Stat stat = statMapper.toEntity(created,
                openAlexService.citationPerYearByAuthorReactive(
                    researcherCreateRequestDto.getOpenAlexId()).block(), OffsetDateTime.now());

            statRepository.save(stat);
        }

        ResearcherListMembershipId id = new ResearcherListMembershipId();
        id.setResearcherId(researcher.get().getId());
        id.setResearcherListId(researcherCreateRequestDto.getResearcherListId());

        ResearcherList researcherList = researcherListRepository.findById(
            researcherCreateRequestDto.getResearcherListId()).orElseThrow();

        ResearcherListMembership researcherListMembership = new ResearcherListMembership();
        researcherListMembership.setId(id);
        researcherListMembership.setResearcher(researcher.get());
        researcherListMembership.setResearcherList(researcherList);

        researcherListMembershipRepository.save(researcherListMembership);

    }

    public void removeResearcherFromList(Long researcherId, Long researcherListId) {
        ResearcherListMembershipId id = new ResearcherListMembershipId();
        id.setResearcherId(researcherId);
        id.setResearcherListId(researcherListId);

        ResearcherListMembership membership = researcherListMembershipRepository.findById(id)
            .orElseThrow();

        researcherListMembershipRepository.delete(membership);
    }

    public ResearcherDto getResearcherById(Long id) {
        return researcherMapper.toDto(researcherRepository.findById(id).orElseThrow());
    }

    public List<ResearcherDto> findAll() {
        return researcherRepository.findAll().stream()
            .map(researcherMapper::toDto).toList();
    }

    public ResearcherStatsDto getResearcherStats(String authorId) {
        // TODO: Will be fetched from DB
        Map<Integer, Integer> citationCountPerYear = openAlexService.citationPerYearByAuthorReactive(
            authorId).block();

        // Use MapStruct to map and combine the data
        return researcherMapper.toResearcherStatsDto(citationCountPerYear);
    }

    public ResearcherListDto addResearcherList(ResearcherListCreateRequestDto requestDto,
        User createdBy) {

        if (researcherListRepository.findByNameAndCreatedById(requestDto.getName(),
                createdBy.getId())
            .isPresent()) {
            throw new RuntimeException(
                String.format("List with name %s is already exist", requestDto.getName()));
        }

        ResearcherList researcherList = researcherMapper.toResearcherListEntity(requestDto,
            createdBy);

        return researcherMapper.toResearcherListDto(
            researcherListRepository.save(researcherList));
    }

    @Transactional
    public ResearcherListDetailDto getResearcherListByDetailId(Long id) {

        ResearcherList researcherList = researcherListRepository.findById(id).orElseThrow();

        List<Researcher> researchers = researcherListMembershipRepository.findAllByResearcherListId(
                id).stream()
            .map(ResearcherListMembership::getResearcher)
            .collect(Collectors.toList());
        ;

        return researcherMapper.toResearcherListDetailDto(researcherList, researchers);
    }

    public List<ResearcherListDto> getAllResearcherList(Long createdById) {
        return researcherListRepository.findAllByCreatedById(createdById).stream()
            .map(researcherMapper::toResearcherListDto).toList();
    }
}
