package com.project.management.springboot.backend.project_management.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.User;

public interface UserRepository extends CrudRepository<User, Long>{
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);
}
