package com.tcsp.digitalwrite.api.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
public class TestController {

    @GetMapping("/api")
    public Map<String, String> index(){
        Map<String, String> data = new HashMap<>();
        data.put("data", "User registered successfully");
        data.put("token-user", UUID.randomUUID().toString());
        data.put("authorities","USER");
        return data;
    }
}
