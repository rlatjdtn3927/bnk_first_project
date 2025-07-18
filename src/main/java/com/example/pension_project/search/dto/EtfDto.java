package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class EtfDto {
    private String prodName;
    private String fundTypeCd;
    private String risk;
    private String manager;
}
