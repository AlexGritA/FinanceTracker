package com.financetracker.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CurrencyService {

    public Double convert(Double amount, String from, String to) {
        if(from.equals(to)) {
            return amount;
        }

        RestClient restClient = RestClient.create();
        String url = "https://api.frankfurter.dev/v2/rate/" + from + "/" + to;

        String response = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(String.class);
            try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            Double rate = root.path("rate").asDouble();
            return amount * rate;
            } catch (Exception e) {
                return amount;
        }
    }
}