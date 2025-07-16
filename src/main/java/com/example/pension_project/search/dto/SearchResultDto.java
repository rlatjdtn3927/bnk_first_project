package com.example.pension_project.search.dto;

import java.util.List;

import com.example.pension_project.jpa.entity.admin.Notice;
import com.example.pension_project.jpa.entity.commodity.DefaultEntity;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.entity.dataroom.Manual;
import com.example.pension_project.jpa.entity.faq.Faq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultDto {
    private List<DefaultEntity> defaultOptions;
    private List<FundEntity> funds;
    private List<ETFEntity> etfs;
    private List<TDFEntity> tdfs;
    private List<Notice> notices;
    private List<Faq> faqs;
    private List<Manual> manuals;

    public static SearchResultDto of(
        List<DefaultEntity> defaultOptions,
        List<FundEntity> funds,
        List<ETFEntity> etfs,
        List<TDFEntity> tdfs,
        List<Notice> notices,
        List<Faq> faqs,
        List<Manual> manuals
    ) {
        return new SearchResultDto(defaultOptions, funds, etfs, tdfs, notices, faqs, manuals);
    }
}
