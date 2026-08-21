package com.financetracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.financetracker.model.Category;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Value("${gemini.api.key}")
    private String apiKey;

    public boolean isRecurring(String description) {
        String prompt = "Is this transaction a recurring subscription or regular bill "
                + "(like Spotify, Netflix, rent, electricity)? "
                + "Transaction: '" + description + "'. "
                + "Answer with only 'true' or 'false', nothing else.";

        RestClient restClient = RestClient.create();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String response = restClient.post()
                .uri(url)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            String answer = root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText()
                    .trim();

            return answer.equalsIgnoreCase("true");
        } catch (Exception e) {
            return false;
        }
    }

    public Category categorize(String description) {
        String prompt = "Categorize this transaction into exactly one of these categories: "
                + "MAT, RESTAURANG, HUSHALL, TRANSPORT, NOJE, HALSA, SPARANDE, OVRIGT. "        + "Transaction: '" + description + "'. "
                + "Respond with only the category word, nothing else.";

        RestClient restClient = RestClient.create();

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=" + apiKey;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        String response = restClient.post()
                .uri(url)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            String categoryText = root
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text").asText()
                    .trim();

            return Category.valueOf(categoryText);
        } catch (Exception e) {
            e.printStackTrace();
            return Category.OVRIGT;
        }
    }



}
