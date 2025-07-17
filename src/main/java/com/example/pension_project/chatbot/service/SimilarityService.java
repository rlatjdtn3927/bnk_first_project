package com.example.pension_project.chatbot.service;

import com.example.pension_project.chatbot.store.VectorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimilarityService {

    private final VectorStore vectorStore;

    public List<VectorStore.VectorEntry> findTopMatches(List<Double> queryEmbedding, int topN) {
        return vectorStore.getAll().stream()
            .sorted(Comparator.comparingDouble(e -> -cosineSimilarity(e.getEmbedding(), queryEmbedding))) // 내림차순
            .limit(topN)
            .toList();
    }

    private double cosineSimilarity(List<Double> a, List<Double> b) {
        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.size(); i++) {
            double va = a.get(i);
            double vb = b.get(i);
            dot += va * vb;
            normA += va * va;
            normB += vb * vb;
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
