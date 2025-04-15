package com.project.management.springboot.backend.project_management.entities;

public class TokenResponse {
    private String token;
    private String username;

    public TokenResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}
