package com.example.pension_project.chatbot.init;

import com.example.pension_project.chatbot.service.EmbeddingService;
import com.example.pension_project.chatbot.service.PdfLoaderService;
import com.example.pension_project.chatbot.store.VectorStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class VectorStoreInitializer {

    private final PdfLoaderService pdfLoaderService;
    private final EmbeddingService embeddingService;
    private final VectorStore vectorStore;

    // @PostConstruct 제거됨
    public void init() {
        System.out.println("📄 PDF 파일 로딩 및 벡터 임베딩 시작...");

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
                    System.out.println("✅ 현재까지 " + count + "개 조각 처리 완료...");
                }
            }
        }

        System.out.println("🔥 초기화 완료! 총 " + count + "개 조각이 저장되었습니다.");
    }
}
