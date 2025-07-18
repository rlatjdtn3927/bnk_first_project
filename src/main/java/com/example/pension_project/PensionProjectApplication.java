package com.example.pension_project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.pension_project.chatbot.init.VectorStoreInitializer;

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
    public void run(String... args) throws Exception {
        // 💾 수동 저장 (분할 저장)
        vectorStoreInitializer.saveVectorStoreToCacheParts();
        System.out.println("✅ 수동 분할 캐시 저장 완료!");
    }
}
