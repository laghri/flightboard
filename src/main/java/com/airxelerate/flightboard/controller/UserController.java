package com.airxelerate.flightboard.controller;

import com.airxelerate.flightboard.dto.request.RegisterRequest;
import com.airxelerate.flightboard.dto.response.ApiResponse;
import com.airxelerate.flightboard.dto.response.UserResponse;
import com.airxelerate.flightboard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(
            @Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "User registered successfully"));
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> registerAdmin(
            @Valid @RequestBody RegisterRequest request) {
        UserResponse response = userService.registerAdmin(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Admin user registered successfully"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<UserResponse> responses = userService.getAllUsers();

        return ResponseEntity.ok(
                ApiResponse.success(responses, "Users retrieved successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);

        return ResponseEntity.ok(
                ApiResponse.success(response, "User retrieved successfully"));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {

        UserResponse response = userService.getCurrentUser();

        return ResponseEntity.ok(
                ApiResponse.success(response, "Current user information retrieved"));
    }
}
