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
        String message = request.get("message");
        String mode = request.getOrDefault("mode", "context"); // 기본은 문맥 기반

        String response = switch (mode) {
            case "simple" -> chatService.getChatResponse(message);
            case "context" -> chatService.getChatResponseWithContext(message);
            default -> chatService.getChatResponseWithContext(message); // fallback
        };

        return Map.of("response", response);
    }
}
