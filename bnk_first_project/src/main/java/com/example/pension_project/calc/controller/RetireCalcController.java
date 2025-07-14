package com.example.pension_project.calc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pension_project.calc.dto.RetireInputDto;
import com.example.pension_project.calc.dto.RetireResultDto;
import com.example.pension_project.calc.service.RetireCalcService;

@RestController
@RequestMapping("/retire")
public class RetireCalcController {
	
	@Autowired
	private RetireCalcService retireCalcService;
	
	@PostMapping("/retire-calc")
	public RetireResultDto calc(@RequestBody RetireInputDto input) {
		
		return retireCalcService.calcResult(input);
	}
	
}
