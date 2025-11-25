package com.airxelerate.flightboard.service;

import com.airxelerate.flightboard.dto.request.RegisterRequest;
import com.airxelerate.flightboard.dto.response.UserResponse;
import com.airxelerate.flightboard.exception.UnauthorizedOperationException;
import com.airxelerate.flightboard.exception.UserAlreadyExistsException;
import com.airxelerate.flightboard.model.Role;
import com.airxelerate.flightboard.model.User;
import com.airxelerate.flightboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username '" + request.getUsername() + "' is already taken"
            );
        }

        Role roleToAssign = Role.USER;
        if (request.getRole() != null && request.getRole() == Role.ADMIN) {
            validateAdminCreation();
            roleToAssign = Role.ADMIN;
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleToAssign)
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Transactional
    public UserResponse registerAdmin(RegisterRequest request) {
        log.info("Registering new admin: {}", request.getUsername());

        validateAdminCreation();

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(
                    "Username '" + request.getUsername() + "' is already taken"
            );
        }

        User admin = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        User savedAdmin = userRepository.save(admin);
        log.info("Admin registered successfully: {}", savedAdmin.getUsername());

        return mapToResponse(savedAdmin);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        return mapToResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return mapToResponse(user);
    }

    private void validateAdminCreation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedOperationException(
                    "You must be authenticated to create admin users"
            );
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new UnauthorizedOperationException(
                    "Only administrators can create admin users"
            );
        }
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .build();
    }
}