package com.swe599.ramp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swe599.ramp.dto.comparison.ComparisonCreateRequestDto;
import com.swe599.ramp.handler.ResponseHandler;
import com.swe599.ramp.model.User;
import com.swe599.ramp.service.ComparisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comparisons")
@RequiredArgsConstructor
@CrossOrigin
public class ComparisonController {

    private final ComparisonService comparisonService;

    @GetMapping
    public ResponseEntity<Object> getAllByCreatedBy(@AuthenticationPrincipal User authUser) {

        return ResponseHandler.generateResponse("Comparisons fetched successfully", HttpStatus.OK,
            comparisonService.getAllComparisons(authUser.getId()));
    }

    @PostMapping
    public ResponseEntity<Object> addComparison(@AuthenticationPrincipal User authUser,
        @RequestBody ComparisonCreateRequestDto requestDto) {

        return ResponseHandler.generateResponse("Comparison is added successfully",
            HttpStatus.OK,
            comparisonService.addComparison(requestDto, authUser));
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getComparisonDetailsById(@PathVariable Long id)
        throws JsonProcessingException {

        return ResponseHandler.generateResponse("Comparison details fetched successfully",
            HttpStatus.OK,
            comparisonService.getComparisonDetails(id));
    }

    @PostMapping("update-data")
    public ResponseEntity<Object> updateResearchersDataByComparison(
        @RequestParam Long comparisonId) {

        comparisonService.updateResearcherDataByComparisonId(comparisonId);

        return ResponseHandler.generateResponse("Researchers data is updated successfully",
            HttpStatus.OK, null
        );
    }
}
