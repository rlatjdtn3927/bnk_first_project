package com.example.pension_project.commodity.dto;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FormDto {
    private Integer[] riskGrade;
    private String[] category;
    private Integer channel;
    private Integer interPeriod;
}
