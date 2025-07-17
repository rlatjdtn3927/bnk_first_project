package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DefaultOptionDto {
    private String prodName;
    private String subProd1;
    private String subProd2;
    private String descUrl;   // "/pdf/default/xxx.pdf"
    private String guideUrl;  // "/guide/default/xxx.pdf"
}