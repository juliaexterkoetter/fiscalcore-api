package com.fiscalcore.api.mapper;

import com.fiscalcore.api.dto.UserRegistrationDTO;
import com.fiscalcore.api.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void testToEntity() {
        UserRegistrationDTO dto = new UserRegistrationDTO("testuser", "password");
        User user = userMapper.toEntity(dto);
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertNull(user.getRoles());
    }
}
