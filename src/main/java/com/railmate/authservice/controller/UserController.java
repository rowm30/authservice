package com.railmate.authservice.controller;

import com.railmate.authservice.model.User;
import com.railmate.authservice.repository.UserRepository;
import com.railmate.authservice.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    CustomUserDetailsService svc;

    @GetMapping("/user")
    public User user() {
        return svc.getCurrentUser();
    }

    @PutMapping("/profile/update")
    public ResponseEntity<?> updateUser(Authentication authentication,
                                        @RequestBody Map<String, String> updates) {
        // the JWTAuthenticationFilter sets authentication.getName() == the email
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

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
