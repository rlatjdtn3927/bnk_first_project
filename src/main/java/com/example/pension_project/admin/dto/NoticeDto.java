package com.example.pension_project.admin.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
public class NoticeDto {
	private Integer b_id;
	private String b_title;
	private String b_content;
	private LocalDateTime b_created_at;
	private Integer b_view;
	
	
}
