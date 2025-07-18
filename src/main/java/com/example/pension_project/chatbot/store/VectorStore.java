// 📁 VectorStore.java (검색 최적화 포함)
package com.example.pension_project.chatbot.store;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.*;

@Component
public class VectorStore {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VectorEntry {
        private String chunkText;
        private List<Double> embedding;
        private String fileName;
        private String category;

        @Setter // 유사도 정렬용 임시 score
        private double score;
    }

    private final List<VectorEntry> vectorEntries = Collections.synchronizedList(new ArrayList<>());

    public void add(String chunkText, List<Double> embedding, String fileName, String category) {
        vectorEntries.add(new VectorEntry(chunkText, embedding, fileName, category, 0.0));
    }

    public List<VectorEntry> getAll() {
        return vectorEntries;
    }

    // ✅ cosine similarity 기반 top-K 검색
    public List<VectorEntry> search(List<Double> queryEmbedding, int topK) {
        return vectorEntries.stream()
            .map(entry -> {
                double score = cosineSimilarity(queryEmbedding, entry.getEmbedding());
                entry.setScore(score);
                return entry;
            })
            .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
            .limit(topK)
            .collect(Collectors.toList());
    }

    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        double dot = 0.0, norm1 = 0.0, norm2 = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            double a = v1.get(i);
            double b = v2.get(i);
            dot += a * b;
            norm1 += a * a;
            norm2 += b * b;
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2) + 1e-8);
    }
}
