// 📄 VectorStoreInitializer.java
package com.example.pension_project.chatbot.init;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.pension_project.chatbot.service.EmbeddingService;
import com.example.pension_project.chatbot.service.PdfLoaderService;
import com.example.pension_project.chatbot.store.VectorStore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VectorStoreInitializer {

    private final PdfLoaderService pdfLoaderService;
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    private static final String CACHE_PATH = "cache/vector-cache.json";

    @PostConstruct
    public void init() {
        try {
            if (Files.exists(Paths.get(CACHE_PATH))) {
                System.out.println("✅ 벡터 캐시 파일 발견: " + CACHE_PATH);
                // ✅ 이미 생성된 vector-cache.json 캐시 파일이 있으면
                // ➤ 그대로 불러오기 (💸 비용 없음 / ⏱ 빠름)
                loadVectorStoreFromCache();
            } else {
            	// ❗ 없으면 처음 실행 → PDF 파싱 + 임베딩
                // ➤ 벡터 생성하고 캐시로 저장 (💸 비용 발생 / ⏱ 느림)
                System.out.println("📄 PDF 파일 파싱 및 임베딩 수행 시작...");
                buildVectorStoreFromPDFs();
                saveVectorStoreToCache();
            }
        } catch (Exception e) {
            System.err.println("❌ 벡터 초기화 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void buildVectorStoreFromPDFs() {
        Map<String, List<String>> chunkMap = pdfLoaderService.loadAllChunks();
        int count = 0;

        for (Map.Entry<String, List<String>> entry : chunkMap.entrySet()) {
            String key = entry.getKey(); // "ETF||filename.pdf"
            String[] parts = key.split("\\|\\|");
            String category = parts[0];
            String filename = parts[1];

            for (String chunk : entry.getValue()) {
                if (chunk == null || chunk.isBlank()) continue;
                List<Double> embedding = embeddingService.getEmbedding(chunk);
                vectorStore.add(chunk, embedding, filename, category);
                count++;
                if (count % 10 == 0) {
                    System.out.println("✅ " + count + "개 조각 임베딩 완료...");
                }
            }
        }

        System.out.println("🔥 PDF 기반 벡터 저장 완료: 총 " + count + "개");
    }

    private void saveVectorStoreToCache() throws Exception {
        System.out.println("💾 캐시 파일 저장 중...");
        Gson gson = new Gson();
        String json = gson.toJson(vectorStore.getAll());
        try (FileWriter writer = new FileWriter(CACHE_PATH)) {
            writer.write(json);
        }
        System.out.println("✅ 캐시 저장 완료: " + CACHE_PATH);
    }

    private void loadVectorStoreFromCache() throws Exception {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(CACHE_PATH)) {
            List<VectorStore.VectorEntry> entries = gson.fromJson(reader, new TypeToken<List<VectorStore.VectorEntry>>(){}.getType());
            for (VectorStore.VectorEntry entry : entries) {
                vectorStore.add(entry.getChunkText(), entry.getEmbedding(), entry.getFileName(), entry.getCategory());
            }
        }
        System.out.println("✅ 캐시 로드 완료! 총 " + vectorStore.size() + "개 조각");
    }
    
    public boolean loadCacheIfExists(String cachePath) {
        try {
            if (!Files.exists(Paths.get(cachePath))) return false;

            Gson gson = new Gson();
            Type listType = new TypeToken<List<VectorStore.VectorEntry>>() {}.getType();
            List<VectorStore.VectorEntry> entries = gson.fromJson(new FileReader(cachePath), listType);

            for (VectorStore.VectorEntry entry : entries) {
                vectorStore.add(entry.getChunkText(), entry.getEmbedding(), entry.getFileName(), entry.getCategory());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void saveCache(String cachePath) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(vectorStore.getAll());
            try (FileWriter writer = new FileWriter(cachePath)) {
                writer.write(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
