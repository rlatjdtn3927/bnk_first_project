package com.example.pension_project.search.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pension_project.search.dto.DefaultOptionDto;
import com.example.pension_project.search.dto.DisclosureDto;
import com.example.pension_project.search.dto.EtfDto;
import com.example.pension_project.search.dto.FaqDto;
import com.example.pension_project.search.dto.FundDto;
import com.example.pension_project.search.dto.ManualDto;
import com.example.pension_project.search.dto.NoticeDto;
import com.example.pension_project.search.dto.PrincipalDto;
import com.example.pension_project.search.dto.SearchResultSection;
import com.example.pension_project.search.dto.TdfDto;
import com.example.pension_project.search.service.DefaultOptionSearchService;
import com.example.pension_project.search.service.AssetContractSearchService;
import com.example.pension_project.search.service.EtfSearchService;
import com.example.pension_project.search.service.FaqSearchService;
import com.example.pension_project.search.service.FundSearchService;
import com.example.pension_project.search.service.ManualSearchService;
import com.example.pension_project.search.service.NoticeSearchService;
import com.example.pension_project.search.service.PrincipalSearchService;
import com.example.pension_project.search.service.TdfSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchRestController {

    /* ───────── 상품 영역 ───────── */
    private final DefaultOptionSearchService defaultOptionService;
    private final PrincipalSearchService     principalService;
    private final FundSearchService          fundService;
    private final EtfSearchService           etfService;
    private final TdfSearchService           tdfService;

    /* ───────── 기타 카테고리 ────── */
    private final ManualSearchService        manualService;
    private final AssetContractSearchService assetContractService;
    private final FaqSearchService           faqService;
    private final NoticeSearchService        noticeService;

    @GetMapping
    public List<SearchResultSection<?>> search(@RequestParam("keyword") String keyword) {

        List<SearchResultSection<?>> sections = new ArrayList<>();

        /* 1) 상품 ─ 디폴트옵션 */
        List<DefaultOptionDto> defaults = defaultOptionService.search(keyword);
        sections.add(new SearchResultSection<>("상품", "디폴트옵션", defaults.size(), defaults));

        /* 2) 상품 ─ 원리금보장 */
//        List<PrincipalDto> principals = principalService.search(keyword);
//        sections.add(new SearchResultSection<>("상품", "원리금보장", principals.size(), principals));

        /* 3) 상품 ─ 펀드 */
        List<FundDto> funds = fundService.search(keyword);
        sections.add(new SearchResultSection<>("상품", "펀드", funds.size(), funds));

        /* 4) 상품 ─ ETF */
        List<EtfDto> etfs = etfService.search(keyword);
        sections.add(new SearchResultSection<>("상품", "ETF", etfs.size(), etfs));

        /* 5) 상품 ─ TDF */
        List<TdfDto> tdfs = tdfService.search(keyword);
        sections.add(new SearchResultSection<>("상품", "TDF", tdfs.size(), tdfs));

        /* 6) 매뉴얼 */
        List<ManualDto> manuals = manualService.search(keyword);
        sections.add(new SearchResultSection<>("매뉴얼", null, manuals.size(), manuals));

        /* 7) 공시 */
        List<DisclosureDto> discs = assetContractService.search(keyword);
        sections.add(new SearchResultSection<>("공시", null, discs.size(), discs));

        /* 8) FAQ */
        List<FaqDto> faqs = faqService.search(keyword);
        sections.add(new SearchResultSection<>("자주찾는질문", null, faqs.size(), faqs));

        /* 9) 공지사항 */
        List<NoticeDto> notices = noticeService.search(keyword);
        sections.add(new SearchResultSection<>("공지사항", null, notices.size(), notices));

        return sections;
    }
}

	