package com.example.pension_project.calc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/calc")
	public String root() {
		return "calc/index";
	}
	
	@GetMapping("/retire-calc")
	public String retireCalc() {
		return "calc/retire-calc";
	}
	
	@GetMapping("/oper-guide")
	public String operGuide() {
		return "oper-guide";
	}
	
}
