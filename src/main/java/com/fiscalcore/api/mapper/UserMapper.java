package com.fiscalcore.api.mapper;

import com.fiscalcore.api.dto.UserRegistrationDTO;
import com.fiscalcore.api.dto.UserResponseDTO;
import com.fiscalcore.api.model.Role;
import com.fiscalcore.api.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User toEntity(UserRegistrationDTO dto) {
        return new User(dto.getUsername(), dto.getPassword(), null);
    }

    public UserResponseDTO toResponseDTO(User user) {
        Set<String> roleNames = user.getRoles() == null ? Set.of() :
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new UserResponseDTO(user.getId(), user.getUsername(), roleNames);
    }
}
