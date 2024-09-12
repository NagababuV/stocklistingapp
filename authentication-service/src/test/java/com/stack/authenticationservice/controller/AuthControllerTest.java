package com.stack.authenticationservice.controller;

import com.stack.authenticationservice.dto.AuthRequest;
import com.stack.authenticationservice.service.AuthService;
import com.stack.authenticationservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    private AuthRequest authRequest;
    private String token;
    private String validToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authRequest = new AuthRequest();
        authRequest.setUsername("user");
        authRequest.setPassword("pass");

        token = "validToken";
        validToken = "Bearer validToken";
        invalidToken = "Bearer invalidToken";
    }

    @Test
    void createAuthenticationToken_ValidCredentials_ShouldReturnOk() {
        when(authService.authenticate(authRequest.getUsername(), authRequest.getPassword())).thenReturn(token);

        ResponseEntity<String> response = authController.createAuthenticationToken(authRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(token, response.getBody());
    }

    @Test
    void createAuthenticationToken_InvalidCredentials_ShouldReturnUnauthorized() {
        when(authService.authenticate(authRequest.getUsername(), authRequest.getPassword())).thenReturn(null);

        ResponseEntity<String> response = authController.createAuthenticationToken(authRequest);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void validateToken_ValidToken_ShouldReturnUserDetails() {
        when(jwtUtil.extractUsername(token)).thenReturn("user");
        when(jwtUtil.validateToken(token, "user")).thenReturn(true);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("username", "user");

        ResponseEntity<?> response = authController.validateToken(validToken);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDetails, response.getBody());
    }

    @Test
    void validateToken_InvalidToken_ShouldReturnUnauthorized() {
        when(jwtUtil.extractUsername(token)).thenReturn("user");
        when(jwtUtil.validateToken(token, "user")).thenReturn(false);

        ResponseEntity<?> response = authController.validateToken(invalidToken);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid Token", response.getBody());
    }

    @Test
    void validateToken_Exception_ShouldReturnUnauthorized() {
        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("Invalid Token"));

        ResponseEntity<?> response = authController.validateToken(invalidToken);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid Token", response.getBody());
    }
}
