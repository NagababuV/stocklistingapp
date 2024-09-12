package com.stack.userprofileservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stack.userprofileservice.dto.UserRequest;
import com.stack.userprofileservice.model.User;
import com.stack.userprofileservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    private UserRequest userRequest;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        userRequest = new UserRequest();
        userRequest.setUsername("Krishna");
        userRequest.setPassword("Naga@123");
        userRequest.setEmail("naga@gmail.com");
        userRequest.setPhone("9876543211");
        userRequest.setCountry("India");

        user = new User();
        user.setUsername("Krishna");
        user.setPassword("Naga@123");
        user.setEmail("naga@gmail.com");
        user.setPhone("9876543211");
        user.setCountry("India");
    }

    @Test
    void register_NewUser_ShouldReturnOk() {
        when(userService.saveUser(userRequest)).thenReturn("User registered successfully");

        ResponseEntity<String> response = userController.register(userRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void register_ExistingUser_ShouldReturnConflict() {
        when(userService.saveUser(userRequest)).thenReturn("User already exists");

        ResponseEntity<String> response = userController.register(userRequest);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void getByUserName_ValidUsername_ShouldReturnUser() {
        when(userService.getUserByUsername("Krishna")).thenReturn(user);

        User result = userController.getByUserName("Krishna");
        assertEquals(user, result);
    }

    @Test
    void getByUserName_InvalidUsername_ShouldReturnNull() {
        when(userService.getUserByUsername("InvalidUser")).thenReturn(null);

        User result = userController.getByUserName("InvalidUser");
        assertEquals(null, result);
    }
}
