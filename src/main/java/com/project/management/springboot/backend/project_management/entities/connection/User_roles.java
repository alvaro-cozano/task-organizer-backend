package com.project.management.springboot.backend.project_management.entities.connection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(UserRolesId.class)
@Table(name = "user_roles")
public class User_roles {

    @Id
    private Long user_id;

    @Id
    private Long role_id;

    public User_roles() {
    }

    public User_roles(Long user_id, Long role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }

    // Getters y setters si los necesitas
}