package com.example.pension_project.chatbot.controller;

import com.example.pension_project.chatbot.service.FileContentService;
import com.example.pension_project.chatbot.service.GroqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatbotController {

    private final FileContentService fileService;
    private final GroqService groqService;

    /**  POST /api/chatbot { "question": "..." }  */
    @PostMapping("/chatbot")
    public String chat(@RequestBody Map<String,String> body) {
        String q = body.getOrDefault("question","");
        String context = fileService.loadAllText();       // 전체 문서 텍스트
        return groqService.askGroq(q, context);           // Groq 답변
    }
}
