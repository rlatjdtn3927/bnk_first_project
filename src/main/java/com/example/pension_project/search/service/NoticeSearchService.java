package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.NoticeDto;
import com.example.pension_project.search.repository.NoticeSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeSearchService {

    private final NoticeSearchRepository repo;

    public List<NoticeDto> search(String keyword) {
        return repo.searchByKeyword(keyword).stream()
                   .map(e -> new NoticeDto(
                           e.getB_title(),
                           e.getB_content(),
                           e.getB_created_at()))
                   .toList();
    }
}