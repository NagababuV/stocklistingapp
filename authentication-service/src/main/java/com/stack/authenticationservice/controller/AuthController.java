package com.stack.authenticationservice.controller;


import com.stack.authenticationservice.dto.AuthRequest;
import com.stack.authenticationservice.service.AuthService;
import com.stack.authenticationservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<String> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        String token = authService.authenticate(authRequest.getUsername(), authRequest.getPassword());
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    @GetMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try {
            // Remove the "Bearer " prefix from the token
            String jwtToken = token.substring(7);

            // Validate the token and extract the username
            String username = jwtUtil.extractUsername(jwtToken);
            boolean isValid = jwtUtil.validateToken(jwtToken, username);

            // Return the user details as a JSON object if the token is valid
            if (isValid) {
                Map<String, Object> userDetails = new HashMap<>();
                userDetails.put("username", username);
             //   userDetails.put("roles", jwtUtil.extractRoles(jwtToken)); // Example, if roles are included in the token

                return ResponseEntity.ok(userDetails);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
    }
}





