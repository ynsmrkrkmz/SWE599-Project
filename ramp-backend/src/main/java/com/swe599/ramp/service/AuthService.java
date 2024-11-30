package com.swe599.ramp.service;

import com.swe599.ramp.model.User;
import com.swe599.ramp.repository.UserRepository;
import com.swe599.ramp.request.auth.AuthenticationRequest;
import com.swe599.ramp.request.auth.RegisterRequest;
import com.swe599.ramp.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public boolean register(RegisterRequest request) {
        var user = User.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        if (userRepository.findByEmail(request.getEmail()).isEmpty()) {
            userRepository.save(user);
        }

        return true;
    }

    public String authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return jwtService.generateToken(user);
    }

    public User getUserDetails(Long userId) {
        return userRepository.getReferenceById(userId);
    }
}
