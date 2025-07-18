package com.example.pension_project.chatbot.init;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AsyncInitializer {

    private final VectorStoreInitializer vectorStoreInitializer;

    @Async
    public void startAsyncInit() {
        vectorStoreInitializer.init();
    }
}

