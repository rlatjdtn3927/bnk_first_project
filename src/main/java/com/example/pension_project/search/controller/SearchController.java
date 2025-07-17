package com.example.pension_project.search.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pension_project.search.dto.SearchResultDto;
import com.example.pension_project.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
	
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResultDto> search(@RequestParam("keyword") String keyword) {
        SearchResultDto result = searchService.searchAll(keyword);
        return ResponseEntity.ok(result);
    }
}
