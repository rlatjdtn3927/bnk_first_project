package com.example.pension_project.notice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {
	private Integer b_id;
	private String b_title;
	private String b_content;
	private LocalDateTime b_created_at;
	private Integer b_view;
}
