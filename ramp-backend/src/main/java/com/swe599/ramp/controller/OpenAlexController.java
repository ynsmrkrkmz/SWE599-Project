package com.swe599.ramp.controller;

import com.swe599.ramp.handler.ResponseHandler;
import com.swe599.ramp.service.OpenAlexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/open-alex")
@RequiredArgsConstructor
@CrossOrigin
public class OpenAlexController {

    private final OpenAlexService openAlexService;

    @GetMapping("search-author")
    public ResponseEntity<Object> searchAuthor(@RequestParam String query) {

        return ResponseHandler.generateResponse("Researchers fetched successfully", HttpStatus.OK,
            openAlexService.searchResearchers(query));
    }
}
