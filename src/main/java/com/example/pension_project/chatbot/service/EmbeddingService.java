package com.example.pension_project.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbeddingService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.embedding.url}")
    private String embeddingUrl;

    @Value("${openai.embedding.model}")
    private String embeddingModel;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Double> getEmbedding(String text) {
        JSONObject body = new JSONObject();
        body.put("model", embeddingModel);
        body.put("input", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(embeddingUrl, request, String.class);

        JSONObject responseJson = new JSONObject(response.getBody());
        JSONArray vector = responseJson.getJSONArray("data").getJSONObject(0).getJSONArray("embedding");
        return vector.toList().stream().map(o -> ((Number) o).doubleValue()).toList();
    }
}
