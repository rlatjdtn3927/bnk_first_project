package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.TdfDto;
import com.example.pension_project.search.repository.TdfSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TdfSearchService {

    private final TdfSearchRepository repo;

    public List<TdfDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new TdfDto(
                           e.getProdName(),
                           e.getFundTypeCd(),
                           e.getRisk(),
                           e.getManager()))
                   .toList();
    }
}
