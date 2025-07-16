package com.example.pension_project.chatbot.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.pension_project.chatbot.store.VectorStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

    @Value("${openai.api.key}")
    private String openaiApiKey;

    @Value("${openai.api.url}")
    private String openaiApiUrl;
    
    private final EmbeddingService embeddingService;
    
    private final SimilarityService similarityService;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getChatResponse(String userMessage) {
        // OpenAI 요청 본문 구성
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
        messages.put(new JSONObject().put("role", "user").put("content", userMessage));

        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openaiApiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // OpenAI API 호출
        ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);

        // 응답에서 메시지 추출
        JSONObject responseBody = new JSONObject(response.getBody());
        return responseBody
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }
    
    public String getChatResponseWithContext(String userQuestion) {
        List<Double> queryEmbedding = embeddingService.getEmbedding(userQuestion);
        List<VectorStore.VectorEntry> relevantChunks = similarityService.findTopMatches(queryEmbedding, 5);

        StringBuilder context = new StringBuilder();
        for (VectorStore.VectorEntry e : relevantChunks) {
            context.append("- [").append(e.getCategory()).append("] ").append(e.getFileName()).append(":\n")
                   .append(e.getChunkText()).append("\n\n");
        }

        // 🔍 간단한 분류 로직 (한글 키워드 기반)
        String taskType;
        String lowerQ = userQuestion.toLowerCase();
        if (lowerQ.contains("비교")) taskType = "compare";
        else if (lowerQ.contains("추천")) taskType = "recommend";
        else taskType = "describe";

        // 🧠 역할에 따라 system 메시지 설정 (한글)
        String systemPrompt = switch (taskType) {
        case "recommend" -> """
            당신은 투자 상담사 역할을 합니다. 사용자의 질문과 관련된 상품 정보를 참고하여 적절한 투자 상품을 추천해 주세요.

            출력 시 다음 기준을 따르세요:

            1. "추천 이유" / "상품 요약" 같은 중간 제목을 사용하세요
            2. 각 항목은 `✅`, `🔹`, `1.` 등으로 구분된 리스트로 정리하세요
            3. 중요한 키워드는 **굵게 강조**하거나 줄바꿈을 통해 눈에 띄게 표현하세요
            4. 문단과 항목마다 줄바꿈을 충분히 넣어 읽기 편하게 구성하세요
            5. 전체 응답은 한국어로, 설명체로 정중하게 작성하세요
            """;

        case "compare" -> """
            당신은 금융 상품 비교 분석 전문가입니다. 아래 문서 정보를 참고하여 사용자의 질문에 따라 상품을 비교해 주세요.

            출력 시 다음 기준을 따르세요:

            1. "장점" / "단점" 등의 소제목으로 구분하세요
            2. 항목은 `1.`, `2.` 또는 `📌`, `🔻` 같은 이모지 리스트로 정리하세요
            3. 항목마다 줄바꿈을 넣어 가독성을 높이고, 핵심 단어는 **강조**하세요
            4. 마지막에 간단한 요약 문장이나 조언을 추가하세요
            5. 전체는 한국어로, 설명체로 정돈된 형식으로 작성하세요
            """;

        default -> """
            당신은 투자 상품을 설명해주는 AI입니다. 관련된 문서 내용을 참고하여 사용자의 질문에 대해 핵심을 중심으로 설명해 주세요.

            출력 시 다음 기준을 지켜 주세요:

            1. 주요 내용은 리스트 형식으로 정리하세요 (예: 🔹, ✅ 등)
            2. 항목 간 줄바꿈을 충분히 사용해 가독성을 높이세요
            3. 너무 긴 문장은 적절히 나누고, 문단을 구분하세요
            4. 가능한 경우 요약 또는 예시도 포함하세요
            5. 전체는 한국어로 친절하고 이해하기 쉽게 작성하세요
            """;
    };
        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
        messages.put(new JSONObject().put("role", "user").put("content", """
            아래는 관련된 금융 상품 정보입니다. 이 내용을 참고해서 질문에 답변해 주세요.

            [문서 요약]
            """ + context + "\n\n[질문]\n" + userQuestion));

        JSONObject body = new JSONObject()
            .put("model", "gpt-3.5-turbo")
            .put("messages", messages)
            .put("temperature", 0.3);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openaiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(openaiApiUrl, entity, String.class);
        JSONObject responseJson = new JSONObject(response.getBody());

        return responseJson.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }
}
