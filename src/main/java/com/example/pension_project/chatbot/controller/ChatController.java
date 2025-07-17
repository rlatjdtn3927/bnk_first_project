package com.example.pension_project.chatbot.controller;

import com.example.pension_project.chatbot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String response = chatService.getChatResponseWithContext(userMessage); // ← 핵심
        return Map.of("response", response);
    }
}
