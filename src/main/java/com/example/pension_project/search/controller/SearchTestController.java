package com.example.pension_project.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchTestController {
	
	
	@GetMapping("/search")
    public String searchPage(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("keyword", keyword); // Thymeleaf에서 검색창 초기화 시 사용 가능
        return "searchResult";
    }

}
