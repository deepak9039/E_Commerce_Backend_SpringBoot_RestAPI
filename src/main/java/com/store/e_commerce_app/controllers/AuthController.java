package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.config.CustomUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }

        Object principal = authentication.getPrincipal();
        Set<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        if (principal instanceof CustomUser) {
            CustomUser cu = (CustomUser) principal;
            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "userId", cu.getUserId(),
                    "username", cu.getUsername(),
                    "roleFromDb", cu.getRole(),
                    "authorities", authorities
            ));
        }

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "principalClass", principal.getClass().getName(),
                "authorities", authorities
        ));
    }
}

