package com.example.pension_project.chatbot.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdfLoaderService {

    @Value("${pdf.etf.path}")
    private String etfPath;

    @Value("${pdf.tdf.path}")
    private String tdfPath;
    
    @Value("${pdf.pg.path}")
    private String pgPath;

    @Value("${pdf.fund.path}")
    private String fundPath;

    @Value("${pdf.default.path}")
    private String defaultPath;
   
    public Map<String, List<String>> loadAllChunks() {
        Map<String, List<String>> result = new HashMap<>();
        result.putAll(loadChunksFromDirectory(etfPath, "ETF"));
        result.putAll(loadChunksFromDirectory(tdfPath, "TDF"));
        result.putAll(loadChunksFromDirectory(pgPath, "PG"));
        result.putAll(loadChunksFromDirectory(fundPath, "FUND"));
        result.putAll(loadChunksFromDirectory(defaultPath, "DEFAULT"));
        return result;
    }

    private Map<String, List<String>> loadChunksFromDirectory(String path, String category) {
        Map<String, List<String>> chunks = new HashMap<>();
        File dir = new File(path);

        System.out.println("📁 시도 중인 경로: " + dir.getAbsolutePath());

        File[] files = dir.listFiles((f) -> f.getName().endsWith(".pdf"));
        if (files == null || files.length == 0) {
            System.out.println("❌ PDF 없음 또는 디렉토리 접근 실패: " + dir.getAbsolutePath());
            return chunks;
        }

        for (File file : files) {
            System.out.println("📄 처리 중: " + file.getName());
            try (PDDocument doc = PDDocument.load(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(doc).replaceAll("\\s+", " ");
                List<String> splitChunks = chunkText(text, 500);
                chunks.put(category + "||" + file.getName(), splitChunks);
            } catch (Exception e) {
                System.out.println("⚠️ 에러: " + file.getName());
                e.printStackTrace();
            }
        }
        return chunks;
    }

    private List<String> chunkText(String text, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        int len = text.length();
        for (int i = 0; i < len; i += chunkSize) {
            chunks.add(text.substring(i, Math.min(len, i + chunkSize)));
        }
        return chunks;
    }
}
