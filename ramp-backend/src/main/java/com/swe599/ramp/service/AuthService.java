package com.swe599.ramp.service;

import com.swe599.ramp.dto.user.UserAuthenticationDto;
import com.swe599.ramp.dto.user.UserAuthenticationRequestDto;
import com.swe599.ramp.dto.user.UserCreateRequestDto;
import com.swe599.ramp.dto.user.UserDto;
import com.swe599.ramp.exceptions.UserAlreadyRegisteredException;
import com.swe599.ramp.mapper.UserMapper;
import com.swe599.ramp.repository.UserRepository;
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
    private final UserMapper userMapper;

    public void register(UserCreateRequestDto request) {
        var user = userMapper.toEntity(request, passwordEncoder);

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyRegisteredException(request.getEmail());
        }

        userRepository.save(user);
    }

    public UserAuthenticationDto authenticate(UserAuthenticationRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        return userMapper.toAuthenticationDto(jwtService.generateToken(user));
    }

    public UserDto getUserDetails(Long userId) {
        return userMapper.toDto(userRepository.getReferenceById(userId));
    }
}
