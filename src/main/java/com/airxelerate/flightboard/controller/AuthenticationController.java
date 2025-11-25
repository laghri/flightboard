package com.airxelerate.flightboard.controller;

import com.airxelerate.flightboard.security.jwt.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authenticationService.authenticate(request);

        return ResponseEntity.ok(
                ApiResponse.success(authResponse, "Authentication successful"));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            return ResponseEntity.ok(
                    ApiResponse.success("Logged out successfully", "Logout successful"));
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("No token found in request"));
    }
}