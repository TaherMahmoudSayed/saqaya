package com.example.saqayatask.controllers;

import com.example.saqayatask.dtos.UserDto;
import com.example.saqayatask.responses.RegisterResponse;
import com.example.saqayatask.services.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    // Add a new user
    @PostMapping("/")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto) {
        // Call the register method from the UserService to register the user
        RegisterResponse response = userService.register(userDto);
        // Return a response entity with the registered user's response
        return ResponseEntity.ok(response);
    }

    // Get a user by ID
    @GetMapping("/{Id}")
    public ResponseEntity<?> getUser(@PathVariable String Id) {
        // Call the getUser method from the UserService to retrieve the user with the given ID
        UserDto dto = userService.getUser(Id);
        // Return a response entity with the retrieved user's DTO
        return ResponseEntity.ok(dto);
    }
}
