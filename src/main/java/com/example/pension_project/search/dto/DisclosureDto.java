package com.example.pension_project.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class DisclosureDto {
    private String docTitle;
    private String fileName;
    private String filePath;
}