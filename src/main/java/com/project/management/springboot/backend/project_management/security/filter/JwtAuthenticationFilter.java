package com.project.management.springboot.backend.project_management.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.security.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.project.management.springboot.backend.project_management.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            String username = (user.getUsername() != null && !user.getUsername().isBlank())
                    ? user.getUsername()
                    : user.getEmail();
            String password = user.getPassword();

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Error al leer las credenciales del usuario", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) authResult
                .getPrincipal();
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        // Buscar el email desde la base de datos
        String username = userDetails.getUsername();
        Optional<User> optionalUser = userRepository.findByUsername(username);
        String email = userRepository.findByUsername(username)
                .map(User::getEmail)
                .orElse("no-email@example.com");
        Long id = optionalUser.map(User::getId).orElse(null);

        // Generar token usando el JwtService
        String token = jwtService.generateToken(username, roles);

        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("id", id);
        body.put("username", username);
        body.put("email", email);
        body.put("message", "Sesión iniciada correctamente");

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed)
            throws IOException, ServletException {

        Map<String, String> body = new HashMap<>();
        body.put("message", "Inicio de sesión incorrecto");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
    }
}
