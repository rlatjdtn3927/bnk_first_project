package com.example.pension_project.chatbot.controller;

import com.example.pension_project.chatbot.service.FileContentService;
import com.example.pension_project.chatbot.service.GroqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final FileContentService fileService;
    private final GroqService groqService;

    /**  POST /api/chatbot { "question": "..." }  */
    @PostMapping("/chatbot")
    public String chat(@RequestBody Map<String, String> body) {
        String q = body.getOrDefault("question", "");

        // 🔹 문서 내용과 파일명 목록
        String context = fileService.loadAllText();           // 전체 문서 텍스트
        List<String> fileNames = fileService.getFileNames();  // 파일 이름 목록

        // 🔹 Groq에 질문
        return groqService.askGroq(q, context, fileNames);
    }
}
