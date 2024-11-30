package com.swe599.ramp.controller;

import com.swe599.ramp.dto.user.UserAuthenticationDto;
import com.swe599.ramp.dto.user.UserAuthenticationRequestDto;
import com.swe599.ramp.dto.user.UserCreateRequestDto;
import com.swe599.ramp.dto.user.UserDto;
import com.swe599.ramp.handler.ResponseHandler;
import com.swe599.ramp.model.User;
import com.swe599.ramp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<Object> register(@RequestBody UserCreateRequestDto request) {
        authService.register(request);
        
        return ResponseHandler.generateResponse("User registered successfully", HttpStatus.OK,
            null);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody UserAuthenticationRequestDto request) {
        UserAuthenticationDto authenticationDto = authService.authenticate(request);

        return ResponseHandler.generateResponse("User login successfully", HttpStatus.OK,
            authenticationDto);
    }

    @GetMapping("/users/me")
    public ResponseEntity<Object> getAuthUserDetails(@AuthenticationPrincipal User authUser) {
        UserDto userDto = authService.getUserDetails(authUser.getId());

        if (userDto == null) {
            return ResponseHandler.generateResponse("Could not find user", HttpStatus.BAD_REQUEST,
                null);
        }

        return ResponseHandler.generateResponse("User found successfully", HttpStatus.OK, userDto);
    }

}
