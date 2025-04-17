package com.project.management.springboot.backend.project_management.utils.mapper;

import java.util.Date;

import com.project.management.springboot.backend.project_management.DTO.UserDTO;
import com.project.management.springboot.backend.project_management.entities.models.User;

public class UserMapper {
    public static User toEntity(UserDTO dto) {
        return new User(
                dto.getFirst_name(),
                dto.getLast_name(),
                dto.getEmail(),
                dto.getUsername(),
                dto.getPassword(),
                false,
                new Date(),
                new Date());
    }
}
