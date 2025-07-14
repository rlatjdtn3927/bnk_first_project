package com.example.pension_project.dataroom.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManualDto {

    private Long id;
    private String title;
    private String systemType;
    private String fileName;
    private LocalDate createdDate;
}

