package com.railmate.authservice.controller;

import com.railmate.authservice.model.User;
import com.railmate.authservice.repository.UserRepository;
import com.railmate.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        try {
            String email = jwtUtil.extractUsername(token);
            boolean isValid = jwtUtil.validateToken(token, email);
            return ResponseEntity.ok(Map.of("valid", isValid, "email", email));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String role = payload.getOrDefault("role", "USER");

        // Save user if not exists
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName("Generated User");
            newUser.setPicture("");
            newUser.setRole(role);
            return userRepository.save(newUser);
        });

        // Update role if needed
        user.setRole(role);
        userRepository.save(user);

        // Generate JWT
        String token = jwtUtil.generateToken(email);

        return ResponseEntity.ok(Map.of("token", token));
    }

}