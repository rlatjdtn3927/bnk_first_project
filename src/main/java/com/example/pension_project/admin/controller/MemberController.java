package com.example.pension_project.admin.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.pension_project.admin.dto.MemberDto;
import com.example.pension_project.admin.service.CertificateService;
import com.example.pension_project.admin.service.MemberService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/member")
@RestController
public class MemberController {
	@Autowired
	private MemberService memberService;

	@Autowired
	private CertificateService certificateService;

	@GetMapping("/getUserList")
	public ResponseEntity<List<MemberDto>> getMemberList() {
		try {
			List<MemberDto> memberList = memberService.getMemberList();
			return ResponseEntity.ok(memberList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	@PostMapping("/logincheck")
	public ResponseEntity<?> loginCheck(@RequestBody MemberDto memberDto, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		if (memberService.logincheck(memberDto)) {
			MemberDto userInfo = memberService.getUserInfoByUsername(memberDto.getUsername());
			session.setAttribute("userInfo", userInfo);

			// userInfo 안에 getRole() 메서드가 있다고 가정
			String roll = userInfo.getRoll();

			response.put("status", "success");
			response.put("message", "로그인에 성공했습니다.");
			response.put("roll", roll);

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Map.of("status", "fail", "message", "아이디 또는 비밀번호가 올바르지 않습니다."));
		}
	}

	@PostMapping("/certificateLogin")
	public ResponseEntity<Map<String, Object>> certificateLogin(
			@RequestParam("certificateFile") MultipartFile certificateFile, @RequestParam("password") String password,
			@RequestParam(value = "timestamp", required = false) String timestamp, HttpSession session) {

		Map<String, Object> response = new HashMap<>();
		System.out.println("certificateLogin 시작!!");
		try {
			// 파일 검증
			if (certificateFile.isEmpty()) {
				response.put("success", false);
				response.put("message", "인증서 파일이 선택되지 않았습니다.");
				return ResponseEntity.badRequest().body(response);
			}

			// 파일 크기 검증 (5MB)
			if (certificateFile.getSize() > 5 * 1024 * 1024) {
				response.put("success", false);
				response.put("message", "파일 크기가 너무 큽니다. (최대 5MB)");
				return ResponseEntity.badRequest().body(response);
			}

			// 파일 확장자 검증 (텍스트 파일만 허용)
			String originalFilename = certificateFile.getOriginalFilename();
			if (originalFilename == null || !isValidTextFile(originalFilename)) {
				response.put("success", false);
				response.put("message", "텍스트 파일만 업로드 가능합니다. (.txt 파일만 지원)");
				return ResponseEntity.badRequest().body(response);
			}

			// 비밀번호 검증
			if (password == null || password.trim().isEmpty()) {
				response.put("success", false);
				response.put("message", "인증서 비밀번호를 입력해주세요.");
				return ResponseEntity.badRequest().body(response);
			}

			// 공인인증서 검증 서비스 호출
			boolean isValid = certificateService.validateCertificate(certificateFile, password);

			if (isValid) {
				// 텍스트 파일 내용에서 사용자 정보 추출
				String username = certificateService.extractUsername(certificateFile, password);

				response.put("success", true);
				response.put("message", "인증서 인증이 완료되었습니다.");
				response.put("username", username);
				response.put("loginMethod", "certificate");
				// 들어온 사용자 이름 확인
				System.out.println("(MemberController) 추출된 username: " + username);
				// username을 받아오면 반다온 값으로 회원정보 가져오기
				session.setAttribute("userInfo", memberService.getUserInfoByUsername(username));
				System.out.println("(MemberController) 세션에 입력된 정보: " + session.getAttribute("userInfo"));

				return ResponseEntity.ok(response);
			} else {
				response.put("success", false);
				response.put("message", "인증서 검증에 실패했습니다. 파일 내용이나 비밀번호를 확인해주세요.");
				return ResponseEntity.badRequest().body(response);
			}

		} catch (IOException e) {
			response.put("success", false);
			response.put("message", "파일 처리 중 오류가 발생했습니다: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "인증서 처리 중 오류가 발생했습니다: " + e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	// 유효한 텍스트 파일 확장자 검증
	private boolean isValidTextFile(String filename) {
		String[] validExtensions = { ".txt" };
		String lowerFilename = filename.toLowerCase();

		for (String extension : validExtensions) {
			if (lowerFilename.endsWith(extension)) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		return ResponseEntity.ok("로그아웃 성공");
	}

	// 사용자 등록
	@PostMapping("/registMember")
	public ResponseEntity<?> registMember(@RequestBody MemberDto member) {
		System.out.println("(MemberController)들어온 사용자 등록정보: " + member);
		try {
			System.out.println("사용자 등록중...");
			
			memberService.registMember(member);
		} catch (Exception e) {
			return ResponseEntity.ok("사용자 등록 성공");
		}
		return null;
	}
	@GetMapping("/getUserDetail")
	public ResponseEntity<?> getUserDetail(@RequestParam("userid") Integer userid) {
	    System.out.println("(MemberController) 들어온 유저 ID: " + userid);
	    MemberDto dto = memberService.getUserInfoById(userid);
	    if (dto != null) {	
	    	System.out.println("(MemberController)가져온 사용자 정보: "+ dto);
	        return ResponseEntity.ok(dto);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("해당 유저를 찾을 수 없습니다.");
	    }
	}
	//사용자 권한 변경
	@PostMapping("/changeUserRoll")
	public ResponseEntity<?>changeUserRoll(@RequestBody MemberDto memberDto){
		System.out.println("권한변경 요청 데이터: "+memberDto);
		MemberDto member = memberService.getUserInfoById(memberDto.getUserid());
		if(member != null) {
			member.setRoll(memberDto.getRoll());
			System.out.println("권한 변경된 userid: "+ member.getUserid());
			System.out.println("저장된 데이터: "+ member);
			//DB에 저장
			memberService.registMember(member);
			return ResponseEntity.ok("권한 변경 성공");
		}
		return ResponseEntity.ok("실패");
	}


}
