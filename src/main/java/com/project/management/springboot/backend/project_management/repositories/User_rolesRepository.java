package com.project.management.springboot.backend.project_management.repositories;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.UserRolesId;
import com.project.management.springboot.backend.project_management.entities.User_roles;

public interface User_rolesRepository extends CrudRepository<User_roles, UserRolesId> {

}