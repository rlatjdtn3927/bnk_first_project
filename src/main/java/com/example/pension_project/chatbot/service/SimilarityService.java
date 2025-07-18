package com.example.pension_project.chatbot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.pension_project.chatbot.store.VectorStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SimilarityService {

    private final VectorStore vectorStore;

    public List<VectorStore.VectorEntry> findTopMatches(List<Double> queryEmbedding, int topN) {
        return vectorStore.search(queryEmbedding, topN); // ✅ VectorStore가 최적화한 search 사용
    }
}

