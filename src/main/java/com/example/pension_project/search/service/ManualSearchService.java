package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.ManualDto;
import com.example.pension_project.search.repository.ManualSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManualSearchService {

    private final ManualSearchRepository repo;

    public List<ManualDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new ManualDto(
                           e.getTitle(),
                           e.getFileName(),
                           e.getCreatedDate()))
                   .toList();
    }
}
