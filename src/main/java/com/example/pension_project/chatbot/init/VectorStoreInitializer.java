// ✅ 최적화된 VectorStoreInitializer.java
package com.example.pension_project.chatbot.init;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
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
    private static final int FLUSH_INTERVAL = 200;

    public void init() {
        try {
        	vectorStore.getAll().clear();
            System.out.println("🧹 vectorStore 초기화 완료 (중복 로딩 방지)");
        	
            if (!vectorStore.getAll().isEmpty()) {
                System.out.println("🟡 이미 메모리에 벡터 있음 → 로딩 생략");
                vectorStore.markInitialized();
                return;
            }

            if (hasCacheFiles()) {
                loadVectorStoreFromCacheParts();
            } else {
                System.out.println("📄 PDF 파일 파싱 및 임베딩 수행 시작...");
                buildVectorStoreFromPDFs();
            }

            vectorStore.markInitialized();

        } catch (Exception e) {
            System.err.println("❌ 벡터 초기화 오류: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private boolean hasCacheFiles() {
        File dir = new File(CACHE_DIR);
        return dir.exists() && dir.isDirectory() &&
               Objects.requireNonNull(dir.listFiles((d, name) -> name.startsWith("vector-cache-part") && name.endsWith(".json"))).length > 0;
    }

    private void buildVectorStoreFromPDFs() {
        Map<String, List<String>> chunkMap = pdfLoaderService.loadAllChunks();
        AtomicInteger count = new AtomicInteger();
        AtomicInteger fileIndex = new AtomicInteger();
        List<VectorStore.VectorEntry> buffer = Collections.synchronizedList(new ArrayList<>());

        try {
            Files.createDirectories(Paths.get(CACHE_DIR));
        } catch (IOException e) {
            System.err.println("❌ cache 디렉토리 생성 실패: " + e.getMessage());
            return;
        }

        chunkMap.entrySet().stream().forEach(entry -> {
            String[] parts = entry.getKey().split("\\|\\|");
            String category = parts[0];
            String filename = parts[1];

            List<String> chunks = entry.getValue();

            for (String chunk : chunks) {
                if (chunk == null || chunk.isBlank()) continue;

                List<Double> embedding = embeddingService.getEmbedding(chunk);
                VectorStore.VectorEntry ve = new VectorStore.VectorEntry(chunk, embedding, filename, category, 0.0);

                synchronized (buffer) {
                    buffer.add(ve);
                    int current = count.incrementAndGet();

                    if (current % FLUSH_INTERVAL == 0) {
                        flushBufferToJson(new ArrayList<>(buffer), fileIndex.getAndIncrement());
                        buffer.clear();
                        System.out.println("✅ " + current + "개 조각 임베딩 및 저장 완료...");
                    }
                }
            }
        });

        if (!buffer.isEmpty()) {
            flushBufferToJson(new ArrayList<>(buffer), fileIndex.getAndIncrement());
            System.out.println("✅ 최종 저장 완료: 총 " + count.get() + "개");
        }

        System.out.println("🔥 PDF 기반 벡터 저장 완료: 총 " + count.get() + "개");
    }


    private void flushBufferToJson(List<VectorStore.VectorEntry> buffer, int fileIndex) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(buffer);
            String fileName = CACHE_DIR + String.format(CACHE_PATTERN, fileIndex);
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(json);
            }
            System.out.println("📁 저장됨: " + fileName);
        } catch (IOException e) {
            System.err.println("❌ 캐시 저장 실패: " + e.getMessage());
        }
    }

    private void loadVectorStoreFromCacheParts() throws Exception {
        //System.out.println("📥 캐시 파일 불러오는 중...");
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
                //System.out.println("✅ 로드 완료: " + file.getName());
            }
        }
        //System.out.println("📦 전체 캐시 로드 완료: 총 " + total + "개 조각");
    }
}