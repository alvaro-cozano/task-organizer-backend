package com.project.management.springboot.backend.project_management.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.project.management.springboot.backend.project_management.entities.TokenResponse;
import com.project.management.springboot.backend.project_management.security.JwtService;
import com.project.management.springboot.backend.project_management.security.TokenJwtConfig;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/check-token")
    public ResponseEntity<?> renewToken(HttpServletRequest request) {
        String token = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
        if (token == null || !token.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token no proporcionado o no válido");
        }

        token = token.replace(TokenJwtConfig.PREFIX_TOKEN, "");

        try {
            Claims claims = jwtService.parseToken(token);
            String username = claims.getSubject();

            // Recuperar roles desde los claims
            String authoritiesJson = (String) claims.get("authorities");

            Collection<GrantedAuthority> authorities = new ArrayList<>();
            try {
                // Deserializar el JSON como un árbol de nodos (JsonNode)
                JsonNode rolesNode = jwtService.getObjectMapper().readTree(authoritiesJson);

                // Recorrer el arreglo de roles y extraer el campo 'name'
                for (JsonNode roleNode : rolesNode) {
                    String roleName = roleNode.path("name").asText();
                    if (!roleName.isEmpty()) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
                    }
                }

                // Generar nuevo token
                String newToken = jwtService.generateToken(username, authorities);

                return ResponseEntity.ok(new TokenResponse(newToken, username));
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Error al procesar los roles del token: " + e.getMessage());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expirado o no válido");
        }
    }
}