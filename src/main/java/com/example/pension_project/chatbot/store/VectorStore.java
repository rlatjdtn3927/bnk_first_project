package com.example.pension_project.chatbot.store;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Data
public class VectorStore {

    @Data
    public static class VectorEntry {
        private String chunkText;
        private List<Double> embedding;
        private String fileName;
        private String category;
    }

    private final List<VectorEntry> store = new ArrayList<>();

    public void add(String chunkText, List<Double> embedding, String fileName, String category) {
        VectorEntry entry = new VectorEntry();
        entry.setChunkText(chunkText);
        entry.setEmbedding(embedding);
        entry.setFileName(fileName);
        entry.setCategory(category);
        store.add(entry);
    }

    public List<VectorEntry> getAll() {
        return store;
    }
}
