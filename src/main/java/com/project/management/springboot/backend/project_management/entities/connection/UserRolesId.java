package com.project.management.springboot.backend.project_management.entities.connection;

import java.io.Serializable;
import java.util.Objects;

public class UserRolesId implements Serializable {

    private Long user_id;
    private Long role_id;

    public UserRolesId() {
    }

    public UserRolesId(Long user_id, Long role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserRolesId))
            return false;
        UserRolesId that = (UserRolesId) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(role_id, that.role_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, role_id);
    }
}