package com.example.pension_project.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GroqService {

    @Value("${groq.api.key}")  private String groqApiKey;
    @Value("${groq.api.url}")  private String groqApiUrl;

    private final RestTemplate restTemplate = new RestTemplate();  // 별도 Bean 필요 X

    public String askGroq(String question, String context) {

        /* 1) 메시지 배열 작성 */
        List<Map<String,String>> messages = new ArrayList<>();
        messages.add(Map.of("role","system",
                "content","다음은 퇴직연금 서식·계약·매뉴얼에서 추출한 자료입니다. " +
                          "사용자 질문에 아래 자료를 참고해 한국어로 답하세요.\n\n"+context));
        messages.add(Map.of("role","user","content",question));

        /* 2) 요청 본문 */
        Map<String,Object> body = new HashMap<>();
        body.put("model","llama3-8b-8192");   // Groq 지원 모델
        body.put("messages",messages);
        body.put("temperature",0.2);

        /* 3) 전송 */
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.setBearerAuth(groqApiKey);

        ResponseEntity<Map> res = restTemplate.exchange(
                groqApiUrl, HttpMethod.POST, new HttpEntity<>(body, h), Map.class);

        /* 4) 답변 꺼내기 */
        List<Map<String,Object>> choices = (List<Map<String,Object>>)res.getBody().get("choices");
        return (String)((Map)choices.get(0).get("message")).get("content");
    }
}
