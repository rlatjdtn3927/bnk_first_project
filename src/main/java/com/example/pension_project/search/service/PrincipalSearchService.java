package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.PrincipalDto;
import com.example.pension_project.search.repository.PrincipalSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalSearchService {

    private final PrincipalSearchRepository repo;

    public List<PrincipalDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new PrincipalDto(
                           e.getBank(),
                           e.getProdName(),
                           e.getMaturityDate()))
                   .toList();
    }
}
