package com.project.management.springboot.backend.project_management.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.management.springboot.backend.project_management.validation.ExistsByEmail;
import com.project.management.springboot.backend.project_management.validation.ExistsByUsername;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private String first_name;
    private String last_name;

    @NotBlank
    @ExistsByEmail
    private String email;

    @NotBlank
    @Size(min = 4, max = 16, message = "debe tener entre 4 y 16 caracteres")
    @ExistsByUsername
    private String username;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserDTO() {
    }

    public UserDTO(
            @NotBlank String first_name,
            @NotBlank String last_name,
            @NotBlank String email,
            @NotBlank @Size(min = 4, max = 16, message = "debe tener entre 4 y 16 caracteres") String username,
            String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
