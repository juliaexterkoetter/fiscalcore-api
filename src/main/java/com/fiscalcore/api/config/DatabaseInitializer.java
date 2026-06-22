package com.fiscalcore.api.config;

import com.fiscalcore.api.model.Role;
import com.fiscalcore.api.model.User;
import com.fiscalcore.api.repository.RoleRepository;
import com.fiscalcore.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private static final String USER_ROLE = "USER";
    private static final String ADMIN_ROLE = "ADMIN";

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    public DatabaseInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role userRole = createRoleIfNotExists(USER_ROLE);
        Role adminRole = createRoleIfNotExists(ADMIN_ROLE);

        createAdminUserIfNotExists(adminRole);

        logger.info("Database initialization completed. Available roles: {}, {}", userRole.getName(), adminRole.getName());
    }

    private Role createRoleIfNotExists(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    logger.info("Creating role: {}", roleName);
                    return roleRepository.save(new Role(null, roleName));
                });
    }

    private void createAdminUserIfNotExists(Role adminRole) {
        userRepository.findByUsername(adminUsername)
                .ifPresentOrElse(
                        existingAdmin -> logger.info("Admin user already exists: {}", existingAdmin.getUsername()),
                        () -> {
                            logger.info("Creating default admin user: {}", adminUsername);

                            User admin = new User(
                                    adminUsername,
                                    passwordEncoder.encode(adminPassword),
                                    Set.of(adminRole)
                            );

                            userRepository.save(admin);
                        }
                );
    }
}