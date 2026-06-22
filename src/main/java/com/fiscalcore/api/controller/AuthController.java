package com.fiscalcore.api.controller;

import com.fiscalcore.api.dto.AuthResponseDTO;
import com.fiscalcore.api.dto.LoginRequestDTO;
import com.fiscalcore.api.dto.UserRegistrationDTO;
import com.fiscalcore.api.dto.UserResponseDTO;
import com.fiscalcore.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO register(@RequestBody @Valid UserRegistrationDTO dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody @Valid LoginRequestDTO dto) {
        return authService.login(dto);
    }
}