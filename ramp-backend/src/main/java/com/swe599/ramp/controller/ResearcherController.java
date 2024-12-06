package com.swe599.ramp.controller;

import com.swe599.ramp.dto.researcher.ResearcherCreateRequestDto;
import com.swe599.ramp.handler.ResponseHandler;
import com.swe599.ramp.service.ResearcherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/researchers")
@RequiredArgsConstructor
@CrossOrigin
public class ResearcherController {

    private final ResearcherService researcherService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody ResearcherCreateRequestDto request) {

        researcherService.create(request);

        return ResponseHandler.generateResponse("User registered successfully", HttpStatus.OK,
            null);
    }
}
