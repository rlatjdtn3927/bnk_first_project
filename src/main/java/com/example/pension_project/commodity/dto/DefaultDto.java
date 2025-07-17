package com.example.pension_project.commodity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DefaultDto {
    private Integer prodId;

    private String prodName;

    private String descUrl;

    private String guideUrl;

    private String subProd1;

    private String subProd2;
    
    private String risk;
    
    private Integer riskGrade;
}

