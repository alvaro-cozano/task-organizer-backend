package com.project.management.springboot.backend.project_management.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.management.springboot.backend.project_management.entities.TokenResponse;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.security.JwtService;
import com.project.management.springboot.backend.project_management.security.TokenJwtConfig;
import com.project.management.springboot.backend.project_management.services.user.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", originPatterns = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


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

    private Map<String, String> verifyGoogleAccessToken(String accessToken) {
        try {
            URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
    
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
    
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.toString());
            String email = jsonNode.get("email").asText();
            String firstName = jsonNode.has("given_name") ? jsonNode.get("given_name").asText() : "";
            String lastName = jsonNode.has("family_name") ? jsonNode.get("family_name").asText() : "";
    
            return Map.of("email", email, "firstName", firstName, "lastName", lastName);
    
        } catch (Exception e) {
            return null;
        }
    }
    

    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) {
        String accessToken = body.get("accessToken");
        
        Map<String, String> userInfo = verifyGoogleAccessToken(accessToken);

        if (userInfo == null || userInfo.get("email") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de Google no válido");
        }
    
        String email = userInfo.get("email");
        String firstName = userInfo.get("firstName");
        String lastName = userInfo.get("lastName");
        
        User user = userRepository.findByEmail(email).orElse(null);
    
        if (user == null) {
            String username = email.split("@")[0];
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword("google-authenticated");
            newUser.setFirst_name(firstName);
            newUser.setLast_name(lastName);
        
            user = userService.save(newUser);
        }
    
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    
        String token = jwtService.createTokenForGoogleUser(user, authorities);
        TokenResponse tokenResponse = new TokenResponse(token, user.getUsername(), user.getEmail());
        return ResponseEntity.ok(tokenResponse);
    }    
}
