package com.example.pension_project.admin.dto;

import java.sql.Date;

import lombok.Data;
@Data
public class PendingDto {
	private Integer p_id;//결재 번호
	private Integer b_id;//게시판 번호
	private String b_title;//게시판 제목
	private String b_content;//게시판 내용
	private Date b_created_at;//생성일
	private String status;//결재 상태
	private String admin_comment;//관리자의견
	private String rejected_comment;//반려사유
}
