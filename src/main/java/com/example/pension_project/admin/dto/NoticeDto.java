package com.example.pension_project.admin.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class NoticeDto {
	private Integer b_id;
	private String b_title;
	private String b_content;
	private Date b_created_at;
	private Integer b_view;
	
	
}
