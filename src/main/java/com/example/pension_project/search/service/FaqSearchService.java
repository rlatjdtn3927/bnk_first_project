package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.FaqDto;
import com.example.pension_project.search.repository.FaqSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqSearchService {

    private final FaqSearchRepository repo;

    public List<FaqDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new FaqDto(
                           e.getQuestion(),
                           e.getAnswer(),
                           e.getMainType()))
                   .toList();
    }
}
