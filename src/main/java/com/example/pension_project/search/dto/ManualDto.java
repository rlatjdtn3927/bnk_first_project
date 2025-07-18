package com.example.pension_project.search.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class ManualDto {
    private String title;
    private String fileName;
    private LocalDate createdDate;
}