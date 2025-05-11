package com.railmate.authservice.controller;

import com.railmate.authservice.model.User;
import com.railmate.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", principal.getAttribute("name"));
        map.put("email", principal.getAttribute("email"));
        map.put("picture", principal.getAttribute("picture"));
        return map;
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal OAuth2User principal,
                                        @RequestBody Map<String, String> updates) {

        String email = principal.getAttribute("email");
        User user = userRepository.findByEmail(email).orElseThrow();

        user.setName(updates.getOrDefault("name", user.getName()));
        user.setPicture(updates.getOrDefault("picture", user.getPicture()));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("status", "updated"));
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
