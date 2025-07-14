package com.example.pension_project.faq.controller;

import com.example.pension_project.faq.dto.FaqDto;
import com.example.pension_project.jpa.entity.faq.Faq;
import com.example.pension_project.jpa.repository.faq.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class FaqController {

    @Autowired
    private FaqRepository faqRepository;

    @GetMapping("/faq")
    public Page<FaqDto> getFaqs(
    		@RequestParam("mainType") String mainType,
            @RequestParam(value = "subType", required = false) String subType,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size	
    ) {
        Pageable pageable = PageRequest.of(page, size);

        // 검색 기능
        if (keyword != null && !keyword.isBlank()) {
            return faqRepository.searchFaq(mainType, subType, keyword, pageable)
                    .map(FaqDto::new);
        }
        
        Page<Faq> faqPage = (subType == null || subType.equals("전체"))
                ? faqRepository.findByMainType(mainType, pageable)
                : faqRepository.findByMainTypeAndSubType(mainType, subType, pageable);

        return faqPage.map(FaqDto::new);
    }



}
