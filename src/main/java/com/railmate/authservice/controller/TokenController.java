package com.railmate.authservice.controller;

import com.railmate.authservice.model.*;
import com.railmate.authservice.repository.*;
import com.railmate.authservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;   //  FIX  need this

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        try {
            String email = jwtUtil.extractUsername(token);
            boolean isValid = jwtUtil.validateToken(token, email);
            return ResponseEntity.ok(Map.of("valid", isValid, "email", email));
        } catch (Exception e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(Map.of("error","Invalid token"));
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String roleStr = payload.getOrDefault("role", "USER");

        /*  FIX  resolve Role entity once, reuse. */
        RoleName roleName = RoleName.valueOf(roleStr.toUpperCase());
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        // create or fetch user
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setName(user.getName() == null ? "Generated User" : user.getName());
        user.setPicture(user.getPicture() == null ? "" : user.getPicture());

        /*  FIX  ensure role is inside the Set<Role>. */
        user.getRoles().add(role);
        userRepository.save(user);

        // produce access token
        String token = jwtUtil.generateToken(email);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
