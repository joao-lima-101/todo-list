package com.todolist.todoserver.mapper;

import com.todolist.todoserver.dto.RegisterRequestDTO;
import com.todolist.todoserver.dto.UserResponseDTO;
import com.todolist.todoserver.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(RegisterRequestDTO dto) {
        if (dto == null) return null;

        User user = new User();

        user.setName(dto.name());
        user.setLastname(dto.lastname());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        return user;
    }

    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
