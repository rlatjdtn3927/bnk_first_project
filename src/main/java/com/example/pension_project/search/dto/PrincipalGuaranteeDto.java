package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrincipalGuaranteeDto {
    private String bank;
    private String prodName;
    private String maturityDate;
}
