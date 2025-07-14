package com.example.pension_project.dataroom.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.pension_project.dataroom.dto.ManualDto;
import com.example.pension_project.dataroom.service.DataroomService;

@Controller
@RequestMapping("/dataroom")
public class DataRoomController {
		
		@Autowired
		private DataroomService dataroomService;
	
		//자료실-규약신고
		@GetMapping("/regulation")
		public String report() {
			return "dataroom/regulation";
		}
		
		//자료실-매뉴얼
		@GetMapping("/manual")
	    public String manualPage(
	            @RequestParam(defaultValue = "",name="keyword") String keyword,
	            @RequestParam(defaultValue = "전체",name="systemType") String systemType,
	            @RequestParam(defaultValue = "0",name="page") int page,
	            Model model) {
			
			if (page < 0) page = 0;  // 음수 방지

	        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdDate"));

	        Page<ManualDto> manualList = dataroomService.getManualList(keyword, systemType, pageable);

	        model.addAttribute("manualList", manualList);
	        model.addAttribute("keyword", keyword);
	        model.addAttribute("systemType", systemType);
	        return "dataroom/manual";
	    }
		
		//자료실->매뉴얼 파일 다운로드
		@GetMapping("/pdf/manual/{fileName:.+}")
	    public ResponseEntity<Resource> downloadManual(@PathVariable("fileName") String fileName) throws IOException {
	        ClassPathResource resource = new ClassPathResource("static/pdf/manual/" + fileName);

	        if (!resource.exists()) {
	            return ResponseEntity.notFound().build();
	        }

	        String mimeType = Files.probeContentType(resource.getFile().toPath());
	        MediaType mediaType = mimeType != null ? MediaType.parseMediaType(mimeType) : MediaType.APPLICATION_OCTET_STREAM;

	        return ResponseEntity.ok()
	                .contentType(mediaType)
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"")
	                .body(resource);
	    }
	}
