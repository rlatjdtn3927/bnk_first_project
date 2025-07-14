package com.example.pension_project.calc.service;

import org.springframework.stereotype.Service;

import com.example.pension_project.calc.dto.RetireInputDto;
import com.example.pension_project.calc.dto.RetireResultDto;

@Service
public class RetireCalcService {

	public RetireResultDto calcResult(RetireInputDto input) {
		
		int workingYears = input.getRetireAge() - input.getCurAge(); //근무 기간 계산
        int retireYears = input.getLifeExp() - input.getRetireAge(); //은퇴 후 생존 연수 계산
        
        //현재 총 생활비
        long curTotalExp = input.getCurExp() * 12L * workingYears;
        //은퇴 후 총 생활비
        long retTotalExp = input.getRetExp() * 12L * retireYears;
        
        //준비 가능한 자금(복리 계산)
        int saveMonths = input.getSaveYears() * 12;
        double monthRate = input.getYieldRate() / 100 / 12; 
        double total = 0;
        
        for(int i = 0; i < saveMonths; i++) {
        	// 복리 수식의 개념 => 미래가치 = (월 저축액 × (1 + 이율)ⁿ)
        	total += input.getSaveAmt() * Math.pow(1 + monthRate, saveMonths - i);
        }
        
        long availAmt = Math.round(total);	//은퇴 시점까지 실제 모을 수 있는 자금
        long needAmt = retTotalExp;			//필요 자금 
        long shortAmt = Math.max(needAmt, availAmt);	//부족 금액(0보다 작으면 0으로 처리)
        
        //결과 객체에 담아서 반환
        RetireResultDto result = new RetireResultDto();
        result.setCurTotalExp(curTotalExp);
        result.setRetTotalExp(retTotalExp);
        result.setAvailAmt(availAmt);
        result.setNeedAmt(needAmt);
        result.setShortAmt(shortAmt);
        
        return result;
	}
}
