package com.example.pension_project.commodity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/commodity-view")
public class CommodityViewController {
	
	@GetMapping("/list")
	public String commodityView() {
		log.info("commodityView...");
		return "commodity/commodityList";
	}
}
