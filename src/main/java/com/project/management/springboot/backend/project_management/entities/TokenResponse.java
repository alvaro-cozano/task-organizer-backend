package com.project.management.springboot.backend.project_management.entities;

public class TokenResponse {
    private Long id;
    private String token;
    private String username;
    private String email;

    public TokenResponse(Long id, String token, String username, String email) {
        this.id = id;
        this.token = token;
        this.username = username;
        this.email = email;
    }

    public Long getid() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
