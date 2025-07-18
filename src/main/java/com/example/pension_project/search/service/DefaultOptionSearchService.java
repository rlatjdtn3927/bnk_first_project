package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.jpa.entity.commodity.DefaultEntity;
import com.example.pension_project.search.dto.DefaultOptionDto;
import com.example.pension_project.search.repository.DefaultOptionSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultOptionSearchService {

    private final DefaultOptionSearchRepository repository;   // 기존에 쓰던 레포지토리
    private static final String PDF_PREFIX   = "/pdf/default/";
    private static final String GUIDE_PREFIX = "/guide/default/";

    /** 키워드 검색 → DTO 리스트 반환 */
    public List<DefaultOptionDto> search(String keyword) {

        // 1) Oracle Text 등으로 엔티티 리스트 조회
        List<DefaultEntity> entities = repository.searchByKeyword(keyword);

        // 2) 엔티티 → DTO 수동 매핑
        return entities.stream()
                       .map(e -> new DefaultOptionDto(
                               e.getProdName(),
                               e.getSubProd1(),
                               e.getSubProd2(),
                               PDF_PREFIX   + e.getDescUrl(),
                               GUIDE_PREFIX + e.getGuideUrl()
                       ))
                       .toList();   // Java 16+ 지원
    }
}
