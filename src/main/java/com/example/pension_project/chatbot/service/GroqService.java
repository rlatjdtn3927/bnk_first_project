package com.example.pension_project.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroqService {

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String askGroq(String question, String context, List<String> fileNames) {

        // ✅ 1. 중복 제거 + 50줄 이하로 자르기
        String trimmedContext = Arrays.stream(context.split("\n"))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .distinct()
            .limit(50)
            .collect(Collectors.joining("\n"));

        // ✅ 2. 시스템 프롬프트 강화
        String systemPrompt = String.join("\n",
            "너는 퇴직연금 서식·매뉴얼을 설명하는 한국어 전문 챗봇이야.",
            "사용자의 질문에 대해 문서 내용을 참고해 간단명료하고 정확하게 한국어로만 답변해야 해.",
            "같은 문장을 반복하지 마.",
            "문서에서 본 내용을 그대로 베끼지 말고 요약해서 말해줘.",
            "",
            "[현재 사이트에 등록된 문서 수: " + fileNames.size() + "개]",
            ""
        ) + trimmedContext;

        // ✅ 3. 메시지 생성
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", question));

        Map<String, Object> body = new HashMap<>();
        body.put("model", "llama3-8b-8192");
        body.put("messages", messages);
        body.put("temperature", 0.2);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        try {
            ResponseEntity<Map> res = restTemplate.exchange(
                groqApiUrl,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
            );
            List<Map<String, Object>> choices = (List<Map<String, Object>>) res.getBody().get("choices");
            return (String) ((Map<?, ?>) choices.get(0).get("message")).get("content");

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.PAYLOAD_TOO_LARGE) {
                return "[❗ 요청이 너무 큽니다. 문서를 자동으로 요약해 다시 질문해주세요.]";
            }
            return "[❗ AI 응답 실패: " + e.getStatusCode() + " - " + e.getMessage() + "]";

        } catch (Exception e) {
            return "[❗ 오류 발생: " + e.getMessage() + "]";
        }
    }
}
