package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.service.UserDltsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DevController {

    @Autowired
    private UserDltsService userDltsService;

    // Create or upgrade a user to admin for development/testing
    @PostMapping("/registerSeller")
    public ResponseEntity<?> createAdmin(@RequestBody UserDlts user) {
        try {
            UserDlts created = userDltsService.createAdmin(user);

            return ResponseEntity.ok(Map.of(
                    "status", "Success",
                    "user", created,
                    "message", "Seller account created successfully. Our team will contact you after approval to start selling your products."
            ));

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest().body(Map.of(
                    "status", "Error",
                    "message", e.getMessage()
            ));
        }
    }
}

