package com.project.management.springboot.backend.project_management;

import java.sql.Date;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.management.springboot.backend.project_management.entities.Role;
import com.project.management.springboot.backend.project_management.entities.Status;
import com.project.management.springboot.backend.project_management.entities.User;
import com.project.management.springboot.backend.project_management.entities.User_roles;
import com.project.management.springboot.backend.project_management.repositories.RoleRepository;
import com.project.management.springboot.backend.project_management.repositories.StatusRepository;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.repositories.User_rolesRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository, UserRepository userRepository,
            StatusRepository statusRepository, User_rolesRepository user_rolesRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role admin = new Role("User", new Date(0), new Date(0));
                Role user = new Role("Admin", new Date(0), new Date(0));

                roleRepository.saveAll(Arrays.asList(admin, user));
            }
            ;

            if (statusRepository.count() == 0) {
                Status pending = new Status("Pendiente", new Date(0), new Date(0));
                Status inProgress = new Status("En Progreso", new Date(0), new Date(0));
                Status finished = new Status("Terminado", new Date(0), new Date(0));
                Status blocked = new Status("Bloqueado", new Date(0), new Date(0));

                statusRepository.saveAll(Arrays.asList(pending, inProgress, finished, blocked));
            }
            ;

            if (userRepository.count() == 0) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword = passwordEncoder.encode("alvaroc");
                Date currentDate = new Date(0);
                User user0 = new User("Alvaro", "Cozano", "alvarocozano@gmail.com", "alvaroc", encodedPassword, true,
                        currentDate, currentDate);

                userRepository.save(user0);
            }

            if (user_rolesRepository.count() == 0) {
                User_roles roleUser = new User_roles(1L, 1L);
                User_roles roleAdmin = new User_roles(1L, 2L);

                user_rolesRepository.saveAll(Arrays.asList(roleUser, roleAdmin));
            }
        };
    }
}