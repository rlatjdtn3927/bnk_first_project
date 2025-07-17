package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.EtfDto;
import com.example.pension_project.search.repository.EtfSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EtfSearchService {

    private final EtfSearchRepository repo;

    public List<EtfDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new EtfDto(
                           e.getProdName(),
                           e.getFundTypeCd(),
                           e.getRisk(),
                           e.getManager()))
                   .toList();
    }
}

