package com.example.pension_project.chatbot.init;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    private static final String CACHE_DIR = "cache/";
    private static final String CACHE_PATTERN = "vector-cache-part-%02d.json";
    private static final int CHUNK_SIZE = 5000;

    @PostConstruct
    public void init() {
        try {
            if (hasCacheFiles()) {
                System.out.println("✅ 벡터 캐시 파일 발견: " + CACHE_DIR);
                loadVectorStoreFromCacheParts();
            } else {
                System.out.println("📄 PDF 파일 파싱 및 임베딩 수행 시작...");
                buildVectorStoreFromPDFs();
                saveVectorStoreToCacheParts();
            }
        } catch (Exception e) {
            System.err.println("❌ 벡터 초기화 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean hasCacheFiles() {
        File dir = new File(CACHE_DIR);
        return dir.exists() && dir.isDirectory() && dir.listFiles((d, name) -> name.startsWith("vector-cache-part")) != null;
    }

    private void buildVectorStoreFromPDFs() {
        Map<String, List<String>> chunkMap = pdfLoaderService.loadAllChunks();
        final int[] count = {0};

        chunkMap.entrySet().parallelStream().forEach(entry -> {
            String key = entry.getKey();
            String[] parts = key.split("\\|\\|");
            String category = parts[0];
            String filename = parts[1];

            List<String> chunks = entry.getValue();
            chunks.parallelStream().forEach(chunk -> {
                if (chunk == null || chunk.isBlank()) return;

                List<Double> embedding = embeddingService.getEmbedding(chunk);
                synchronized (vectorStore) {
                    vectorStore.add(chunk, embedding, filename, category);
                }

                synchronized (count) {
                    count[0]++;
                    if (count[0] % 10 == 0) {
                        System.out.println("✅ " + count[0] + "개 조각 임베딩 완료...");
                    }
                }
            });
        });

        System.out.println("🔥 PDF 기반 벡터 저장 완료: 총 " + count[0] + "개");
    }

    public void saveVectorStoreToCacheParts() throws Exception {
        System.out.println("💾 캐시 파일 분할 저장 중...");
        Gson gson = new Gson();
        List<VectorStore.VectorEntry> all = vectorStore.getAll();

        int fileIndex = 0;
        for (int i = 0; i < all.size(); i += CHUNK_SIZE) {
            List<VectorStore.VectorEntry> sublist = all.subList(i, Math.min(i + CHUNK_SIZE, all.size()));
            String json = gson.toJson(sublist);
            String fileName = CACHE_DIR + String.format(CACHE_PATTERN, fileIndex++);
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(json);
            }
            System.out.println("✅ 저장: " + fileName);
        }

        System.out.println("📌 총 " + fileIndex + "개 파일로 분할 저장 완료");
    }

    private void loadVectorStoreFromCacheParts() throws Exception {
        System.out.println("📥 캐시 파일 불러오는 중...");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<VectorStore.VectorEntry>>() {}.getType();

        File dir = new File(CACHE_DIR);
        File[] files = dir.listFiles((d, name) -> name.startsWith("vector-cache-part") && name.endsWith(".json"));

        if (files == null || files.length == 0) return;

        Arrays.sort(files, Comparator.comparing(File::getName));

        int total = 0;
        for (File file : files) {
            try (FileReader reader = new FileReader(file)) {
                List<VectorStore.VectorEntry> entries = gson.fromJson(reader, listType);
                for (VectorStore.VectorEntry entry : entries) {
                    vectorStore.add(entry.getChunkText(), entry.getEmbedding(), entry.getFileName(), entry.getCategory());
                    total++;
                }
                System.out.println("✅ 로드 완료: " + file.getName());
            }
        }

        System.out.println("📦 전체 캐시 로드 완료: 총 " + total + "개 조각");
    }
}
