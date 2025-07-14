package com.example.pension_project.calc.dto;

import lombok.Data;

@Data
public class RetireInputDto {
	
	private int curAge;	//현재나이
	private int retireAge;	//은퇴나이
	private int lifeExp;	//기대수명
	private Long curExp;	//현재 월 생활비
	private Long retExp;	//은퇴 후 예상 월 생활비
	private int saveYears;	//저축기간
	private Long saveAmt;	//매월 저축액
	private double yieldRate;	//투자 수익률
	private double infRate;		//물가상승률

}
