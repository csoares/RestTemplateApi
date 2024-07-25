package com.example.demo.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SimpleRequest {

    private static final String JSON_URL = "https://jsonplaceholder.typicode.com/todos";


    @GetMapping("/print")
    public ResponseEntity<String> printInformation() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(JSON_URL, String.class);
        return ResponseEntity.ok().header("Content-Type", "application/json").body(response);
    }


    @GetMapping("/filter")
    public List<Map<String, Object>> getTodoTitlesAndIds() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(JSON_URL, String.class);

        // Convert response to JSON array
        JSONArray jsonArray = new JSONArray(response);

        // List to store todo information
        List<Map<String, Object>> todoList = new ArrayList<>();

        // Extract title and id from each JSON object in the array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String title = jsonObject.getString("title");

            Map<String, Object> todoInfo = new HashMap<>();
            todoInfo.put("id", id);
            todoInfo.put("title", title);

            todoList.add(todoInfo);
        }

        return todoList;
    }



}
