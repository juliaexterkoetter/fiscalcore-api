package com.fiscalcore.api.security;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() throws Exception {
        jwtTokenProvider = new JwtTokenProvider();

        Field secretField = JwtTokenProvider.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        secretField.set(jwtTokenProvider, "testsecrettestsecrettestsecrettestsecr");

        Field validityField = JwtTokenProvider.class.getDeclaredField("validityInMilliseconds");
        validityField.setAccessible(true);
        validityField.set(jwtTokenProvider, 3600000L);

        jwtTokenProvider.init();
    }

    @Test
    void testCreateAndValidateToken() {
        String username = "testuser";
        List<String> roles = List.of("ADMIN");
        String token = jwtTokenProvider.createToken(username, roles);

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));

        Claims claims = jwtTokenProvider.getClaims(token);
        assertEquals(username, claims.getSubject());

        Object rolesClaim = claims.get("roles");
        List<String> tokenRoles;
        if (rolesClaim instanceof List<?>) {
            List<?> rawRoles = (List<?>) rolesClaim;
            tokenRoles = rawRoles.stream().map(String::valueOf).collect(Collectors.toList());
        } else {
            tokenRoles = List.of();
        }
        assertNotNull(tokenRoles);
        assertTrue(tokenRoles.contains("ADMIN"));
    }
}
