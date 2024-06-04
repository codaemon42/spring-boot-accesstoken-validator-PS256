package com.codaemon.tokenvalidator.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/protected-resource")
public class ProtectedController {

    @GetMapping
    public ResponseEntity<Map<String, String>> protectedRoute() {
        return ResponseEntity.ok(Map.of("message", "This is a protected resource"));
    }
}
