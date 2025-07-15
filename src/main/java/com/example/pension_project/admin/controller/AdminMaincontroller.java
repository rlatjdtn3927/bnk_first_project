package com.example.pension_project.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
@Controller
public class AdminMaincontroller {
	
	@GetMapping("/adminLogin")
	public String adminLogin() {
		return "/admin/login";
	}
	@GetMapping("/approval")
	public String approval(HttpSession session,Model model) {
		model.addAttribute("userInfo",session.getAttribute("userInfo"));
		if(session.getAttribute("userInfo") == null) {
			return "/admin/login";
		}
		return "/admin/approval";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/admin/login";
	}
	@GetMapping("/registForm")
	public String registForm() {
		return "/admin/registForm";
	}
}
