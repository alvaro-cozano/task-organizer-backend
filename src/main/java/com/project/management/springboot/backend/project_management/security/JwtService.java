package com.project.management.springboot.backend.project_management.security;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import static com.project.management.springboot.backend.project_management.security.TokenJwtConfig.SECRET_KEY;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME = 3600000; // 1 hora
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final TypeReference<Collection<SimpleGrantedAuthority>> GRANTED_AUTHORITY_LIST_TYPE = new TypeReference<>() {
    };

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String createToken(Authentication authResult, String email) {
        String username = authResult.getName();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        return generateToken(username, roles);
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> roles) {
        try {
            String authoritiesJson = objectMapper.writeValueAsString(roles);

            return Jwts.builder()
                    .subject(username)
                    .claim("authorities", authoritiesJson)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SECRET_KEY)
                    .compact();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar el token JWT", e);
        }
    }

    public Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
