package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.FundDto;
import com.example.pension_project.search.repository.FundSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundSearchService {

    private final FundSearchRepository repo;

    public List<FundDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new FundDto(
                           e.getProdName(),
                           e.getFundTypeCd(),
                           e.getRisk(),
                           e.getManager()))
                   .toList();
    }
}