package com.stack.userprofileservice.controller;

import com.stack.userprofileservice.dto.UserRequest;
import com.stack.userprofileservice.model.User;
import com.stack.userprofileservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRequest userRequest) {
        String result = userService.saveUser(userRequest);
        if ("User already exists".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result); // Use 409 Conflict status for existing user
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/profiles")
    public User getByUserName(@RequestParam("username") String username) {
      return userService.getUserByUsername(username);

    }

}


