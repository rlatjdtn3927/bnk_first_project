package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class TdfDto {
    private String prodId;
    private String prodName;
    private String fundTypeCd;
    private String risk;
    private String manager;
}