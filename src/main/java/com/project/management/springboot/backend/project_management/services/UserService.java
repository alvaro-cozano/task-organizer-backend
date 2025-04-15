package com.project.management.springboot.backend.project_management.services;

import java.util.List;

import com.project.management.springboot.backend.project_management.entities.User;

public interface UserService {
    
    List<User> findAll();

    User save(User user);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
