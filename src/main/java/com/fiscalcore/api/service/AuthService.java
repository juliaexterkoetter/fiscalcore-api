package com.fiscalcore.api.service;

import com.fiscalcore.api.dto.AuthResponseDTO;
import com.fiscalcore.api.dto.LoginRequestDTO;
import com.fiscalcore.api.dto.UserRegistrationDTO;
import com.fiscalcore.api.dto.UserResponseDTO;
import com.fiscalcore.api.exception.InvalidCredentialsException;
import com.fiscalcore.api.mapper.UserMapper;
import com.fiscalcore.api.model.User;
import com.fiscalcore.api.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(UserService userService,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder,
                       UserMapper userMapper) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponseDTO register(UserRegistrationDTO dto) {
        User user = userMapper.toEntity(dto);
        User saved = userService.registerUser(user);
        return userMapper.toResponseDTO(saved);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        User foundUser = userService.findByUsername(dto.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(dto.getPassword(), foundUser.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        if (foundUser.getRoles() == null || foundUser.getRoles().isEmpty()) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(
                foundUser.getUsername(),
                foundUser.getRoles().stream().map(r -> r.getName()).toList()
        );

        return new AuthResponseDTO(token);
    }
}
