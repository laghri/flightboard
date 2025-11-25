package com.airxelerate.flightboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.airxelerate.flightboard.dto.request.LoginRequest;
import com.airxelerate.flightboard.dto.response.AuthResponse;
import com.airxelerate.flightboard.security.jwt.JwtUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    
    public AuthResponse authenticate(LoginRequest request) {
        log.info("Authenticating user: {}", request.getUsername());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);
        
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");
        
        log.info("User authenticated successfully: {}", request.getUsername());
        
        return new AuthResponse(token, userDetails.getUsername(), role);
    }
}
