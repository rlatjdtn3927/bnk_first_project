package com.example.pension_project.search.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
public class NoticeDto {
    private String bTitle;
    private String bContent;
    private LocalDateTime bCreatedAt;
}