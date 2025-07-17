package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchItemDto {
    private String title;        // ex. 상품명, 공지제목, 질문
    private String snippet;      // 요약 설명 (ex. 상품 정보, 답변, 본문 등)
    private String subTitle;     // ex. 운용사, 만기일 등
    private String category;     // 상품 / 매뉴얼 / 공시사항 / 자주찾는질문 / 공지사항
    private String type;         // 상품 종류: 펀드, ETF, TDF, 디폴트옵션, 원리금보장
    private String link;         // 상세보기 or 다운로드 링크
    private String date;         // 작성일자 (선택)
    private String actionText;   // 버튼 텍스트
    
    
}
