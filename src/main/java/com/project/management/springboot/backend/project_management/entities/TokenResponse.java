package com.project.management.springboot.backend.project_management.entities;

public class TokenResponse {
    private String token;
    private String username;
    private String email;

    public TokenResponse(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
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
