package com.example.pension_project.calc.dto;

import lombok.Data;

@Data
public class RetireResultDto {
	
	private Long curTotalExp;	//현재 총 생활비
	private Long retTotalExp;	//은퇴 후 총 생활비
	private Long availAmt;		//준비 가능 자금
	private Long shortAmt;		//부족 자금
	private Long needAmt;		//필요 은퇴 자금

}
