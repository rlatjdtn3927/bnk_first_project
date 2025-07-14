package com.example.pension_project.disclosure.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.pension_project.disclosure.dto.AssetContractDto;
import com.example.pension_project.disclosure.service.DisclosureService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/disclosure")
@RequiredArgsConstructor
public class DisclosureController {
	
	private final DisclosureService DisclosureService;
	
	//공시-운용 금액/수익률 
	@GetMapping("/investment_dashboard")
	public String investment() {
		return "disclosure/investment_dashboard";
	}
	
	//공시-수수료
	@GetMapping("/charge")
	public String charge() {
		return "disclosure/charge";
	}
	
	//공시-운용 방법 및 방법별 수익률
	@GetMapping("/method_dashboard")
	public String method() {
		return "disclosure/method_dashboard";
	}
	
	//공시->자산 및 자산관리 약관
	@GetMapping("/asset")
	public String asset(Model model) {
		List<AssetContractDto> contracts = DisclosureService.getAllContracts();
        model.addAttribute("contractList", contracts);
		return "disclosure/asset";
	}
	
	//공시->자산 관리 약관 서식파일 다운로드
	@GetMapping("/pdf/asset/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fileName) throws IOException {
	    // classpath 경로로 PDF 리소스 접근
	    ClassPathResource resource = new ClassPathResource("static/pdf/asset/" + fileName);

	    if (!resource.exists()) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF)
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
	            .body(resource);
	}
}
