package com.example.demo.controller;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/todos")
public class TodoController {

    private final WebClient webClient;

    @Autowired
    public TodoController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
    }

    @GetMapping("/print")
    public Mono<ResponseEntity<String>> printInformation() {
        return webClient.get()
                .uri("/todos")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> ResponseEntity.ok().header("Content-Type", "application/json").body(response));
    }

    @GetMapping("/filter")
    public Mono<List<Map<String, Object>>> getTodoTitlesAndIds() {
        return webClient.get()
                .uri("/todos")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    JSONArray jsonArray = new JSONArray(response);
                    List<Map<String, Object>> todoList = new ArrayList<>();
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
                });
    }
}
