package com.example.pension_project.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NoticeViewController {

	@GetMapping("/dataroom/notice")
	public String noticePage() {
		return "notice";
	}
	
	// 공지 상세
	@GetMapping("/notice/{id}")
	public String noticeDetailPage(@PathVariable("id") Integer id, Model model) {
	    model.addAttribute("noticeId", id);
	    return "notice-detail";
	}
}
