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
        vectorStoreInitializer.init();
    }

}
