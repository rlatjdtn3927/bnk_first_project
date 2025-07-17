package com.example.pension_project;

import com.example.pension_project.chatbot.init.VectorStoreInitializer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PensionProjectApplication implements CommandLineRunner {

    private final VectorStoreInitializer vectorStoreInitializer;

    public PensionProjectApplication(VectorStoreInitializer vectorStoreInitializer) {
        this.vectorStoreInitializer = vectorStoreInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(PensionProjectApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // ✅ vector-cache.json 파일 존재 여부에 따라 초기화 수행
        vectorStoreInitializer.init(); // 내부에서 캐시 존재 여부 판단
    }
}
