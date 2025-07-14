package com.example.pension_project.admin.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.pension_project.admin.dto.MemberDto;
import com.example.pension_project.admin.dto.NoticeDto;
import com.example.pension_project.admin.dto.PendingDto;
import com.example.pension_project.admin.service.NoticeService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/notice")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	
	// 권한 검증 메서드
	private boolean isAuthorized(String username) {
		/**if(username.equals("master") || username.equals("admin")) {
			return true;
		}
		return false;
		**/
		return true;
	}
	
	// 권한 에러 응답 생성 메서드
	private ResponseEntity<String> unauthorizedResponse() {
		return ResponseEntity.status(403).body("접근 권한이 없습니다. MASTER 권한이 필요합니다.");
	}
	
	@GetMapping("/getNoticeList")
	public ResponseEntity<?> getNoticeList(@RequestHeader(value = "Username", required = false) String username) {
		// 공지사항 조회는 모든 사용자에게 허용 (필요시 권한 검증 추가 가능)
		try {
			List<NoticeDto> noticeList = noticeService.getNotionList();
			return ResponseEntity.ok(noticeList);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("공지사항 조회 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
	
	@GetMapping("/getPendingList")
	public ResponseEntity<?> getPendingList(HttpSession session) {
		// MASTER 권한 검증
		MemberDto userInfo = (MemberDto) session.getAttribute("userInfo");
		if (!isAuthorized(userInfo.getUsername())) {
			System.out.println("사용자 인증실패");
			return unauthorizedResponse();
		}
		System.out.println("GetPendingList 사용자 인증 통과");
		
		try {
			List<PendingDto> pendingList = noticeService.getPendingList();
			return ResponseEntity.ok(pendingList);
		} catch (Exception e) {
			System.out.println("목록 조회 실패!!!!");
			return ResponseEntity.badRequest().body("결재 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
	
	@PostMapping("/writeApproval")
	public ResponseEntity<String> writeApproval(
			@RequestBody PendingDto pendingDto, 
			HttpSession session) {
		
		try {
			noticeService.registrationRequest(pendingDto);
			return ResponseEntity.ok("데이터저장 성공");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("데이터저장 실패: " + e.getMessage());
		}
	}
	
	@GetMapping("/getPendingById")
	public ResponseEntity<?> viewPendingDetail(
			@RequestParam("p_id") Integer p_id,
			@RequestHeader(value = "Username", required = false) String username) {
		
		/** MASTER 권한 검증
		if (!isAuthorized(username)) {
			return unauthorizedResponse();
		}
		**/
		
		try {
			Optional<PendingDto> dto = noticeService.getpendinListByPid(p_id);
			if (dto.isPresent()) {
				return ResponseEntity.ok(dto.get());
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("데이터 조회 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
	
	@PostMapping("/uploadApprove")
	public ResponseEntity<String> uploadApprove(
			@RequestBody PendingDto pendingDto,
			HttpSession session) {
		
		// MASTER 권한 검증
		MemberDto userInfo = (MemberDto) session.getAttribute("userInfo");
		if(!userInfo.getRoll().equals("MASTER")) {
			return unauthorizedResponse();
		}
		
		try {
			// 넘어오는 데이터: pending 아이디, 상태, 반려라면 반려 사유
			// 넘어온P_id를 이용해서 pending Table에서 데이터를 가져옴
			Optional<PendingDto> pendingData = noticeService.getpendinListByPid(pendingDto.getP_id());
			
			if (!pendingData.isPresent()) {
				return ResponseEntity.badRequest().body("해당 결재 문서를 찾을 수 없습니다.");
			}
			
			PendingDto existingData = pendingData.get();
			
			if (pendingDto.getStatus().equals("승인")) {
				// 결재 대기 테이블의 상태 변경
				PendingDto dto = new PendingDto();
				dto.setP_id(pendingDto.getP_id());
				dto.setB_id(existingData.getB_id());
				dto.setAdmin_comment(existingData.getAdmin_comment());
				dto.setB_title(existingData.getB_title());
				dto.setB_content(existingData.getB_content());
				dto.setB_created_at(existingData.getB_created_at());
				dto.setStatus("승인");
				dto.setRejected_comment(existingData.getRejected_comment());
				
				noticeService.updatePendingStatus(dto);
				
				// 결재가 승인된 내용들을 공지사항 데이터베이스에 저장
				NoticeDto notice = new NoticeDto();
				notice.setB_title(existingData.getB_title());
				notice.setB_content(existingData.getB_content());
				noticeService.registNotice(notice);
				
				return ResponseEntity.ok("승인 처리가 완료되었습니다.");
				
			} else if (pendingDto.getStatus().equals("반려")) {
				// 반려 처리
				PendingDto dto = new PendingDto();
				dto.setP_id(pendingDto.getP_id());
				dto.setB_id(existingData.getB_id());
				dto.setAdmin_comment(existingData.getAdmin_comment());
				dto.setB_title(existingData.getB_title());
				dto.setB_content(existingData.getB_content());
				dto.setB_created_at(existingData.getB_created_at());
				dto.setStatus("반려");
				dto.setRejected_comment(pendingDto.getRejected_comment()); // 반려 사유 설정
				
				noticeService.updatePendingStatus(dto);
				
				return ResponseEntity.ok("반려 처리가 완료되었습니다.");
			} else {
				return ResponseEntity.badRequest().body("잘못된 상태값입니다.");
			}
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("처리 중 오류가 발생했습니다: " + e.getMessage());
		}
	}
}