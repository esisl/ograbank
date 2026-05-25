package com.example.bankcards.service;

import com.example.bankcards.dto.AuthRequestDto;
import com.example.bankcards.dto.AuthResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public AuthResponseDto register(AuthRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже зарегистрирован");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole("USER"); // По умолчанию USER. ADMIN создаётся отдельно.
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
        return new AuthResponseDto(token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(AuthRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Неверный email или пароль"));
        
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Неверный email или пароль");
        }
        
        String token = jwtUtil.generateToken(user.getEmail(), user.getId(), user.getRole());
        return new AuthResponseDto(token);
    }
}