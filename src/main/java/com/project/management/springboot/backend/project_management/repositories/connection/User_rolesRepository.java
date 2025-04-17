package com.project.management.springboot.backend.project_management.repositories.connection;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.connection.UserRolesId;
import com.project.management.springboot.backend.project_management.entities.connection.User_roles;

public interface User_rolesRepository extends CrudRepository<User_roles, UserRolesId> {

}