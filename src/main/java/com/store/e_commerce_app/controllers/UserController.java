package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.config.CustomUser;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.service.UserDltsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
public class UserController {

    @Autowired
    UserDltsService userDltsService;

    @Autowired
    private AuthenticationManager authManager;

    @GetMapping("/")
    public String Home() {
        return "Welcome to the E-Commerce Application!";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody UserDlts userDlts, HttpSession session) {

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userDlts.getEmail(),
                            userDlts.getPassword()
                    )
            );

            // 🔥 Set Authentication into SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 🔥 Attach Spring Security context to session
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            CustomUser user = (CustomUser) authentication.getPrincipal();

            // Enforce SUPER_ADMIN approval for admin/seller roles
            String role = user.getRole();
            boolean isAdminLike = false;
            if (role != null) {
                String normalized = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                if (normalized.equals("ROLE_ADMIN") || normalized.equals("ROLE_SUPER_ADMIN") || normalized.equals("ROLE_SUPER_USER")) {
                    isAdminLike = true;
                }
            }

            if (isAdminLike) {
                // load entity to check approval flag
                UserDlts full = userDltsService.findByUserId(user.getUserId());
                if (full != null && (full.getApprovedBySuperAdmin() == null || !full.getApprovedBySuperAdmin())) {
                    // deny access for admin-like users not approved by a SUPER_ADMIN
                    // also clear authentication from context
                    SecurityContextHolder.clearContext();
                    session.removeAttribute("SPRING_SECURITY_CONTEXT");
                    return ResponseEntity.status(403).body(Map.of(
                            "status", "FAILED",
                            "message", "Your account is under review. Please wait for admin approval."
                    ));
                }
            }

            // update last login time for audit
            userDltsService.updateLastLoginTime(user.getUserId());

            return ResponseEntity.ok(
                    Map.of(
                            "status", "SUCCESS",
                            "userId", user.getUserId(),
                            "userName", user.getUserName(),
                            "email", user.getUsername(),
                            "role", user.getAuthorities().iterator().next().getAuthority()
                    )
            );

        } catch (Exception ex) {
            return ResponseEntity.status(401).body(
                    Map.of(
                            "status", "FAILED",
                            "message", "Invalid email or password"
                    )
            );
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(
                Map.of(
                        "status", "SUCCESS",
                        "message", "Logged out successfully"
                )
        );
    }


}
