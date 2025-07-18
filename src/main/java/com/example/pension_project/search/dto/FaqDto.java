package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class FaqDto {
    private String question;
    private String answer;
    private String mainType;
}