package com.swe599.ramp.controller;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.dto.researcher.ResearcherListCreateRequestDto;
import com.swe599.ramp.handler.ResponseHandler;
import com.swe599.ramp.model.User;
import com.swe599.ramp.service.OpenAlexService;
import com.swe599.ramp.service.ResearcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/researchers")
@RequiredArgsConstructor
@CrossOrigin
public class ResearcherController {

    private final ResearcherService researcherService;
    private final OpenAlexService openAlexService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ResearcherCreateRequestDto request) {

        researcherService.addResearcher(request);

        return ResponseHandler.generateResponse("User registered successfully", HttpStatus.OK,
            null);
    }

    @GetMapping("stats")
    public ResponseEntity<Object> getStatsByAuthor(@RequestParam String authorId) {

        return ResponseHandler.generateResponse("Researchers fetched successfully", HttpStatus.OK,
            researcherService.getResearcherStats(authorId));
    }

    @PostMapping("search-author")
    public ResponseEntity<Object> searchAuthor(@RequestParam String query) {

        return ResponseHandler.generateResponse("Researchers fetched successfully", HttpStatus.OK,
            openAlexService.searchResearchers(query));
    }

    @GetMapping("researcher-list")
    public ResponseEntity<Object> getResearcherList(@AuthenticationPrincipal User authUser) {

        return ResponseHandler.generateResponse("Researcher List fetched successfully",
            HttpStatus.OK,
            researcherService.getAllResearcherList(authUser.getId()));
    }

    @PostMapping("add-researcher-list")
    public ResponseEntity<Object> addResearcherList(@AuthenticationPrincipal User authUser,
        @RequestBody ResearcherListCreateRequestDto requestDto) {

        return ResponseHandler.generateResponse("Researcher List is added successfully",
            HttpStatus.OK,
            researcherService.addResearcherList(requestDto, authUser));
    }
}
