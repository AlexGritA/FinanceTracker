package com.financetracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class CurrencyService {

    public Double convert(Double amount, String from, String to) {
        if(from.equals(to)) {
            return amount;
        }

            RestClient restClient = RestClient.create();
            String url = "https://api.frankfurter.app/latest?amount=" + amount + "&from=" + from + "&to=" + to;

            String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
            try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("rates").path(to).asDouble();
            } catch (Exception e) {
                return amount;
        }
    }

}