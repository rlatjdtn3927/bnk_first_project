package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.DisclosureDto;
import com.example.pension_project.search.repository.AssetContractSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetContractSearchService {

    private final AssetContractSearchRepository repo;

    public List<DisclosureDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new DisclosureDto(
                           e.getTitle(),
                           e.getFilePath()))
                   .toList();
    }
}