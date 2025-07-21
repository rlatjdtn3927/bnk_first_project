package com.example.pension_project.notice.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pension_project.jpa.entity.admin.Notice;
import com.example.pension_project.jpa.repository.admin.NoticeRepository;
import com.example.pension_project.notice.dto.NoticeSummaryDto;
import com.example.pension_project.notice.service.NoticeUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notice")
@RequiredArgsConstructor
public class NoticeRestController {

	private final NoticeUserService noticeService;
    private final NoticeRepository noticeRepository;


	// 공지 목록 (페이징)
	@GetMapping
	public ResponseEntity<?> getNotices(@RequestParam(name = "page") int page,
	                                    @RequestParam(name = "size") int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    Page<NoticeSummaryDto> result = noticeService.getNotices(pageable);
	    return ResponseEntity.ok(result);
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<?> getNoticeDetail(@PathVariable("id") Integer id) {
        Optional<Notice> noticeOpt = noticeRepository.findById(id);
        return noticeOpt.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/top")
    public List<NoticeSummaryDto> getTop5Notices() {
        Pageable topFive = PageRequest.of(0, 5);
        return noticeRepository.findTop5ByCustomQuery(topFive)
                .stream()
                .map(notice -> new NoticeSummaryDto(
                    notice.getB_id(),
                    notice.getB_title(),
                    notice.getB_created_at(),
                    notice.getB_view()
                ))
                .collect(Collectors.toList());
    }









}