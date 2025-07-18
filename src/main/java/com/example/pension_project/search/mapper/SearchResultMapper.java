package com.example.pension_project.search.mapper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.pension_project.jpa.entity.admin.Notice;
import com.example.pension_project.jpa.entity.commodity.DefaultEntity;
import com.example.pension_project.jpa.entity.commodity.ETFEntity;
import com.example.pension_project.jpa.entity.commodity.FundEntity;
import com.example.pension_project.jpa.entity.commodity.TDFEntity;
import com.example.pension_project.jpa.entity.dataroom.Manual;
import com.example.pension_project.jpa.entity.disclosure.AssetContract;
import com.example.pension_project.jpa.entity.faq.Faq;
import com.example.pension_project.search.dto.SearchItemDto;

public class SearchResultMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static List<SearchItemDto> fromDefaultList(List<DefaultEntity> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (DefaultEntity e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getProdName())
                    .snippet(e.getSubProd1() + "<br>" + e.getSubProd2())
                    .subTitle("디폴트옵션")
                    .category("상품")
                    .type("디폴트옵션")
                    .link("/default/" + e.getProdId())
                    .actionText("상품 보기")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromFundList(List<FundEntity> list, String type) {
        List<SearchItemDto> result = new ArrayList<>();
        for (FundEntity e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getProdName())
                    .snippet("1개월 수익률: " + e.getOneMonth() + "%")
                    .subTitle(e.getManager())
                    .category("상품")
                    .type(type)
                    .link("/fund/" + e.getProdId())
                    .actionText("상품 보기")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromEtfList(List<ETFEntity> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (ETFEntity e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getProdName())
                    .snippet("위험등급: " + e.getRisk())
                    .subTitle(e.getManager())
                    .category("상품")
                    .type("ETF")
                    .link("/etf/" + e.getProdId())
                    .actionText("상품 보기")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromTdfList(List<TDFEntity> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (TDFEntity e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getProdName())
                    .snippet("위험등급: " + e.getRisk())
                    .subTitle(e.getManager())
                    .category("상품")
                    .type("TDF")
                    .link("/tdf/" + e.getProdId())
                    .actionText("상품 보기")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromNoticeList(List<Notice> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (Notice e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getB_title())
                    .snippet(e.getB_content())
                    .date(e.getB_created_at().toString().substring(0, 10))
                    .category("공지사항")
                    .type("")
                    .link("/notice/" + e.getB_id())
                    .actionText("자세히 보기")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromFaqList(List<Faq> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (Faq e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getQuestion())
                    .snippet(e.getAnswer())
                    .category("자주찾는질문")
                    .type("")
                    .link("/faq")
                    .actionText("질문 보기")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromManualList(List<Manual> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (Manual e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getTitle())
                    .snippet(e.getFileName())
                    .category("매뉴얼")
                    .type("")
                    .link("/manuals/download/" + e.getId())
                    .actionText("다운로드")
                    .build());
        }
        return result;
    }

    public static List<SearchItemDto> fromAssetList(List<AssetContract> list) {
        List<SearchItemDto> result = new ArrayList<>();
        for (AssetContract e : list) {
            result.add(SearchItemDto.builder()
                    .title(e.getTitle())
                    .snippet(e.getFileName())
                    .category("공시")
                    .type("")
                    .link(e.getFilePath())
                    .actionText("다운로드")
                    .build());
        }
        return result;
    }
}
