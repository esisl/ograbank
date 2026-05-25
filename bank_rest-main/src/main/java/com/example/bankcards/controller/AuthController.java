package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthRequestDto;
import com.example.bankcards.dto.AuthResponseDto;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.AccessDeniedException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto register(@Valid @RequestBody AuthRequestDto dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto dto) {
        return authService.login(dto);
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(Authentication auth) {
        if (auth.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return Map.of(
                "id", userDetails.getId(),
                "email", userDetails.getUsername(),
                "role", userDetails.getAuthorities().iterator().next().getAuthority()
            );
        }
        throw new AccessDeniedException("Not authenticated");
    }
}