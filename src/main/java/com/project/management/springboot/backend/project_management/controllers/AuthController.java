package com.project.management.springboot.backend.project_management.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.project.management.springboot.backend.project_management.entities.TokenResponse;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.security.JwtService;
import com.project.management.springboot.backend.project_management.security.TokenJwtConfig;
import com.project.management.springboot.backend.project_management.services.user.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

            String authoritiesJson = (String) claims.get("authorities");
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            JsonNode rolesNode = jwtService.getObjectMapper().readTree(authoritiesJson);
            for (JsonNode roleNode : rolesNode) {
                String roleName = roleNode.path("name").asText();
                if (!roleName.isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));
                }
            }

            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }
            String email = user.getEmail();

            String newToken = jwtService.generateToken(username, authorities);

            return ResponseEntity.ok(new TokenResponse(newToken, username, email));

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al procesar los roles del token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token expirado o no válido");
        }
    }

    @GetMapping("/oauth2/callback/google")
    public void googleCallback(HttpServletResponse response, OAuth2AuthenticationToken authentication) throws IOException {
        OAuth2User oAuth2User = authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(email);
            newUser.setPassword("");
            newUser.setRoles(new ArrayList<>());
            return userService.save(newUser);
        });

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        String token = jwtService.generateToken(user.getUsername(), authorities);

        String frontendRedirectUrl = "http://localhost:5173/";

        String redirectUrl = frontendRedirectUrl + "?" +
                "token=" + token +
                "&username=" + URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8) +
                "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
    }
}
