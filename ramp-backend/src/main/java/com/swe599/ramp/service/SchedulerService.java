package com.swe599.ramp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swe599.ramp.mapper.StatMapper;
import com.swe599.ramp.model.Researcher;
import com.swe599.ramp.model.Stat;
import com.swe599.ramp.repository.ResearcherRepository;
import com.swe599.ramp.repository.StatRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class SchedulerService {

    private final OpenAlexService openAlexService;
    private final ResearcherService researcherService;
    private final ResearcherRepository researcherRepository;
    private final StatRepository statRepository;
    private final StatMapper statMapper;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void runJobs() {

        List<Researcher> researchers = researcherRepository.findAll();

        researcherService.updateResearchersData(researchers);
    }


}

