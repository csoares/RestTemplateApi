package com.example.demo.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/countries")
public class CountryController {

    private final WebClient webClient;

    @Autowired
    public CountryController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://restcountries.com")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024)) // 16 MB buffer limit
                        .build())
                .build();
    }

    private static final String JSON_URL = "/v3.1/all";

    @GetMapping
    public Mono<List<Map<String, String>>> getCountryInfo() {
        return webClient.get()
                .uri(JSON_URL)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    JSONArray jsonArray = new JSONArray(response);

                    List<Map<String, String>> countryList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String countryName = jsonObject.getJSONObject("name").getString("common");
                        JSONArray capitalArray = jsonObject.optJSONArray("capital");
                        String capital = (capitalArray != null && capitalArray.length() > 0) ? capitalArray.getString(0) : "No Capital";
                        String flag = jsonObject.optString("flag", "No Flag");
                        JSONArray timezonesArray = jsonObject.optJSONArray("timezones");
                        String timezone = (timezonesArray != null && timezonesArray.length() > 0) ? timezonesArray.getString(0) : "No Timezone";
                        JSONObject languagesObject = jsonObject.optJSONObject("languages");
                        String language = (languagesObject != null) ? languagesObject.keys().next() : "No Language";

                        Map<String, String> countryInfo = new HashMap<>();
                        countryInfo.put("countryName", countryName);
                        countryInfo.put("capital", capital);
                        countryInfo.put("flag", flag);
                        countryInfo.put("timezone", timezone);
                        countryInfo.put("language", language);

                        countryList.add(countryInfo);
                    }

                    return countryList;
                });
    }
}
