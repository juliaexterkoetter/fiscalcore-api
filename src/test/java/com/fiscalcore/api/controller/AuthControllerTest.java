package com.fiscalcore.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiscalcore.api.dto.AuthResponseDTO;
import com.fiscalcore.api.dto.LoginRequestDTO;
import com.fiscalcore.api.dto.UserRegistrationDTO;
import com.fiscalcore.api.dto.UserResponseDTO;
import com.fiscalcore.api.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(authService))
                .build();
    }

    @Test
    void testRegister() throws Exception {
        UserRegistrationDTO request = new UserRegistrationDTO("alice", "secret");
        UserResponseDTO response = new UserResponseDTO(1L, "alice", Set.of("USER"));

        when(authService.register(any(UserRegistrationDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.roles[0]").value("USER"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("alice", "secret");
        AuthResponseDTO response = new AuthResponseDTO("jwt-token");

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}