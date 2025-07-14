package com.example.pension_project.chatbot.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pension_project.chatbot.service.FileContentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileTestController {

    private final FileContentService fileContentService;

    @GetMapping("/api/file-list")
    public List<String> getAllFileNames() {
        return fileContentService.listAllParsedFiles();
    }
    
    @GetMapping("/api/test-files")
    public String getAllFileContents() {
        try {
            return fileContentService.loadAllText();
        } catch (Exception e) {
            // 전체 스택 트레이스를 로그로만 남기고,
            // 사용자에겐 "파일 파싱 실패"라고만 출력하도록 하자!
            e.printStackTrace();
            return "파일 파싱 중 문제가 발생했습니다.";
        }
    }
}
