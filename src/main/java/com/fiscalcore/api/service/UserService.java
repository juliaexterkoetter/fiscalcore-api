package com.fiscalcore.api.service;

import com.fiscalcore.api.exception.BadRequestException;
import com.fiscalcore.api.model.Role;
import com.fiscalcore.api.model.User;
import com.fiscalcore.api.repository.RoleRepository;
import com.fiscalcore.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final String DEFAULT_ROLE = "USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        logger.info("Registering user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName(DEFAULT_ROLE)
                .orElseGet(() -> roleRepository.save(new Role(null, DEFAULT_ROLE)));
        user.setRoles(Set.of(role));

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        logger.info("Looking up user by username: {}", username);
        return userRepository.findByUsername(username);
    }
}
