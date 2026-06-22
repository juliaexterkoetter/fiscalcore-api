package com.fiscalcore.api.service;

import com.fiscalcore.api.exception.BadRequestException;
import com.fiscalcore.api.model.Role;
import com.fiscalcore.api.model.User;
import com.fiscalcore.api.repository.RoleRepository;
import com.fiscalcore.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserService(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void testRegisterUserSuccess() {
        User user = new User("testuser", "password", null);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedpassword");

        Role role = new Role(1L, "USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User registered = userService.registerUser(user);

        assertNotNull(registered);
        assertEquals(1L, registered.getId());
        assertEquals("testuser", registered.getUsername());
        assertEquals("encodedpassword", registered.getPassword());
        assertNotNull(registered.getRoles());
        assertTrue(registered.getRoles().stream().anyMatch(roleItem -> "USER".equals(roleItem.getName())));

        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).encode("password");
        verify(roleRepository).findByName("USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUserDuplicateUsername() {
        User existing = new User("testuser", "encodedpassword", Set.of(new Role(1L, "USER")));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(existing));

        User user = new User("testuser", "password", null);

        assertThrows(BadRequestException.class, () -> userService.registerUser(user));

        verify(userRepository).findByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testFindByUsername() {
        User user = new User("testuser", "encodedpassword", Set.of(new Role(1L, "USER")));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> found = userService.findByUsername("testuser");

        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());

        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        Optional<User> found = userService.findByUsername("unknown");

        assertTrue(found.isEmpty());

        verify(userRepository).findByUsername("unknown");
    }
}