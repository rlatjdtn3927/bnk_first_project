package com.example.pension_project.notice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoticeSummaryDto {
	private Integer b_id;
	private String b_title;
	private LocalDateTime b_created_at;
	private Integer b_view;
}