package com.project.management.springboot.backend.project_management.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.models.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
