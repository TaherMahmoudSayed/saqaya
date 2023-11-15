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
    @PostMapping("/")
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDto userDto)  {
        RegisterResponse response=userService.register(userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{Id}")

    public ResponseEntity<?> addUser(@PathVariable String Id){
        UserDto dto=userService.getUser(Id);
        return ResponseEntity.ok(dto);
    }
}
