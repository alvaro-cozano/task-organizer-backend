package com.project.management.springboot.backend.project_management.DTO;

import jakarta.validation.constraints.NotBlank;

public class UserReferenceDTO {
    @NotBlank
    private String email;

    public UserReferenceDTO() {
    }

    public UserReferenceDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
