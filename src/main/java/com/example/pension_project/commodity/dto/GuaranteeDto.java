package com.example.pension_project.commodity.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuaranteeDto {
    private int prod_id;
    private String bank;
    private String prodName;
    private String maturityDate;
    private Double dbYn;
    private Double dcYn;
    private Double irpYn;
    private String termsUrl;
    private String descUrl;
    private String threeMonth;
}
