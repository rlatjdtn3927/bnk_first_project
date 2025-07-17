package com.example.pension_project.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.search.dto.SearchItemDto;
import com.example.pension_project.search.mapper.SearchResultMapper;
import com.example.pension_project.search.repository.AssetContractSearchRepository;
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
    private final AssetContractSearchRepository assetConRepo;

    public List<SearchItemDto> searchAll(String keyword) {
        List<SearchItemDto> result = new ArrayList<>();

        result.addAll(SearchResultMapper.fromDefaultList(defaultOptionRepo.searchByKeyword(keyword)));
        result.addAll(SearchResultMapper.fromFundList(fundRepo.searchByKeyword(keyword), "펀드"));
        result.addAll(SearchResultMapper.fromEtfList(etfRepo.searchByKeyword(keyword)));
        result.addAll(SearchResultMapper.fromTdfList(tdfRepo.searchByKeyword(keyword)));
        result.addAll(SearchResultMapper.fromNoticeList(noticeRepo.searchByKeyword(keyword)));
        result.addAll(SearchResultMapper.fromFaqList(faqRepo.searchByKeyword(keyword)));
        result.addAll(SearchResultMapper.fromManualList(manualRepo.searchByKeyword(keyword)));
        result.addAll(SearchResultMapper.fromAssetList(assetConRepo.searchByKeyword(keyword)));

        return result;
    }


}