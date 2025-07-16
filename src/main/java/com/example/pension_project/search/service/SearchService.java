package com.example.pension_project.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.jpa.entity.admin.Notice;
import com.example.pension_project.jpa.entity.commodity.DefaultEntity;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.entity.dataroom.Manual;
import com.example.pension_project.jpa.entity.faq.Faq;
import com.example.pension_project.search.dto.SearchResultDto;
import com.example.pension_project.search.repository.DefaultOptionSearchRepository;
import com.example.pension_project.search.repository.EtfSearchRepository;
import com.example.pension_project.search.repository.FaqSearchRepository;
import com.example.pension_project.search.repository.FundSearchRepository;
import com.example.pension_project.search.repository.ManualSearchRepository;
import com.example.pension_project.search.repository.NoticeSearchRepository;
import com.example.pension_project.search.repository.TdfSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final DefaultOptionSearchRepository defaultOptionRepo;
    private final FundSearchRepository fundRepo;
    private final EtfSearchRepository etfRepo;
    private final TdfSearchRepository tdfRepo;
    private final NoticeSearchRepository noticeRepo;
    private final FaqSearchRepository faqRepo;
    private final ManualSearchRepository manualRepo;

    public SearchResultDto searchAll(String keyword) {
        List<DefaultEntity> defaultOptions = defaultOptionRepo.searchByKeyword(keyword);
        List<FundEntity> funds = fundRepo.searchByKeyword(keyword);
        List<ETFEntity> etfs = etfRepo.searchByKeyword(keyword);
        List<TDFEntity> tdfs = tdfRepo.searchByKeyword(keyword);
        List<Notice> notices = noticeRepo.searchByKeyword(keyword);
        List<Faq> faqs = faqRepo.searchByKeyword(keyword);
        List<Manual> manuals = manualRepo.searchByKeyword(keyword);

        return SearchResultDto.of(defaultOptions, funds, etfs, tdfs, notices, faqs, manuals);
    }
}