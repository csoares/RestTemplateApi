package com.example.demo.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/countries")
public class CountryController {

    private static final String JSON_URL = "https://restcountries.com/v3.1/all";

    @GetMapping
    public List<Map<String, String>>  getCountryInfo() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(JSON_URL, String.class);

        // Convert response to JSON array
        JSONArray jsonArray = new JSONArray(response);

        // List to store country information
        List<Map<String, String>> countryList = new ArrayList<>();

        // Extract country information from each JSON object in the array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String countryName = jsonObject.getJSONObject("name").getString("common");
            JSONArray capitalArray = jsonObject.optJSONArray("capital");
            String capital = (capitalArray != null && !capitalArray.isEmpty()) ? capitalArray.getString(0) : "No Capital";
            String flag = jsonObject.optString("flag", "No Flag");
            JSONArray timezonesArray = jsonObject.optJSONArray("timezones");
            String timezone = (timezonesArray != null && !timezonesArray.isEmpty()) ? timezonesArray.getString(0) : "No Timezone";
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
    }

}

