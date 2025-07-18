package com.example.pension_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.pension_project.chatbot.init.AsyncInitializer;

@SpringBootApplication
@EnableAsync
public class PensionProjectApplication implements CommandLineRunner {

    private final AsyncInitializer asyncInitializer;

    public PensionProjectApplication(AsyncInitializer asyncInitializer) {
        this.asyncInitializer = asyncInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(PensionProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        asyncInitializer.startAsyncInit(); // 진짜 비동기로 실행됨
    }
}

