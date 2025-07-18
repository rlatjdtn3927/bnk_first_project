package com.example.pension_project.search.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResultSection<T> {
    private String category;     // "상품"
    private String subCategory;  // 예: "디폴트옵션", "ETF" 등
    private int count;
    private List<T> items;
}

