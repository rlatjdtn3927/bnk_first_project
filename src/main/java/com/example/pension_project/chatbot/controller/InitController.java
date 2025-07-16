package com.example.pension_project.chatbot.controller;

import com.example.pension_project.chatbot.init.VectorStoreInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitController {

    private final VectorStoreInitializer initializer;

    @GetMapping("/init")
    public String initialize() {
        initializer.init();
        return "✅ 벡터 저장소 초기화가 완료되었습니다.";
    }
}
