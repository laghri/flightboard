package com.airxelerate.flightboard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.airxelerate.flightboard.dto.request.LoginRequest;
import com.airxelerate.flightboard.dto.response.ApiResponse;
import com.airxelerate.flightboard.dto.response.AuthResponse;
import com.airxelerate.flightboard.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authenticationService.authenticate(request);

        return ResponseEntity.ok(
                ApiResponse.success(authResponse, "Authentication successful"));
    }
}