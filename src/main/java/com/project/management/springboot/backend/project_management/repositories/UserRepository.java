package com.project.management.springboot.backend.project_management.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    public List<User> findUsersByBoards_Id(Long boardId);

}
