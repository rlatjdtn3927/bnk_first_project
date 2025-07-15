package com.example.pension_project.admin.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CertificateService {
	// 미리 정의된 인증 문자열들 (실제 운영에서는 DB나 설정 파일에서 관리)
	private static final String[] VALID_CERTIFICATE_CONTENTS = { "MASTER_CERTIFICATE_2024", "ADMIN_ACCESS_TOKEN_2024"};

	/**
	 * 텍스트 파일 내용과 비밀번호를 검증합니다.
	 * 
	 * @param certificateFile 텍스트 파일 (인증서 역할)
	 * @param password        인증서 비밀번호
	 * @return 검증 성공 여부
	 * @throws IOException 파일 처리 오류
	 */
	public boolean validateCertificate(MultipartFile certificateFile, String password) throws IOException {
		try {
			// 파일 내용을 문자열로 읽기
			String fileContent = new String(certificateFile.getBytes(), StandardCharsets.UTF_8).trim();

			System.out.println("업로드된 파일 내용: " + fileContent);
			System.out.println("입력된 비밀번호: " + password);

			// 1. 파일 내용이 유효한 인증서 내용인지 확인
			boolean isValidContent = false;
			for (String validContent : VALID_CERTIFICATE_CONTENTS) {
				if (validContent.equals(fileContent)) {
					isValidContent = true;
					System.out.println("유효한 인증서 내용 발견: " + validContent);
					break;
				}
			}

			if (!isValidContent) {
				System.out.println("유효하지 않은 인증서 내용");
				return false;
			}

			// 2. 비밀번호 검증 (각 인증서 내용별로 다른 비밀번호)
			return validatePassword(fileContent, password);

		} catch (Exception e) {
			System.err.println("인증서 검증 중 오류 발생: " + e.getMessage());
			return false;
		}
	}

	/**
	 * 인증서 내용에 따른 비밀번호 검증
	 */
	private boolean validatePassword(String certificateContent, String password) {
		switch (certificateContent) {
		case "MASTER_CERTIFICATE_2024":
			return "1234".equals(password);
		case "ADMIN_ACCESS_TOKEN_2024":
			return "1234".equals(password);
		
		default:
			return false;
		}
	}

	/**
	 * 텍스트 파일에서 사용자명을 추출합니다.
	 * 
	 * @param certificateFile 텍스트 파일
	 * @param password        비밀번호
	 * @return 추출된 사용자명
	 * @throws IOException 파일 처리 오류
	 */
	public String extractUsername(MultipartFile certificateFile, String password) throws IOException {
		try {
			String fileContent = new String(certificateFile.getBytes(), StandardCharsets.UTF_8).trim();

			// 인증서 내용에 따라 사용자명 반환
			switch (fileContent) {
			case "MASTER_CERTIFICATE_2024":
				return "master";
			case "ADMIN_ACCESS_TOKEN_2024":
				return "admin";
			default:
				return "GUEST";
			}

		} catch (Exception e) {
			System.err.println("사용자명 추출 중 오류 발생: " + e.getMessage());
			return "GUEST"; // 기본값
		}
	}
}
