package com.project.management.springboot.backend.project_management;

import java.sql.Date;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.management.springboot.backend.project_management.entities.connection.User_board;
import com.project.management.springboot.backend.project_management.entities.connection.User_card;
import com.project.management.springboot.backend.project_management.entities.connection.User_roles;
import com.project.management.springboot.backend.project_management.entities.models.Board;
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.Role;
import com.project.management.springboot.backend.project_management.entities.models.Status;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.BoardRepository;
import com.project.management.springboot.backend.project_management.repositories.CardRepository;
import com.project.management.springboot.backend.project_management.repositories.RoleRepository;
import com.project.management.springboot.backend.project_management.repositories.StatusRepository;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_boardRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_cardRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_rolesRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            RoleRepository roleRepository,
            UserRepository userRepository,
            StatusRepository statusRepository,
            User_rolesRepository user_rolesRepository,
            BoardRepository boardRepository,
            User_boardRepository user_boardRepository,
            CardRepository cardRepository,
            User_cardRepository user_cardRepository) {
        return args -> {
            if (roleRepository.count() == 0) {
                Role admin = new Role("User", new Date(0), new Date(0));
                Role user = new Role("Admin", new Date(0), new Date(0));

                roleRepository.saveAll(Arrays.asList(admin, user));
            }
            ;

            if (userRepository.count() == 0) {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String encodedPassword1 = passwordEncoder.encode("alvaroc");
                String encodedPassword2 = passwordEncoder.encode("pablocoz");
                Date currentDate = new Date(0);
                User user0 = new User("Alvaro", "Cozano", "alvarocozano@gmail.com", "alvaroc", encodedPassword1, true,
                        currentDate, currentDate);
                User user1 = new User("Pablo", "Cozano", "pablocozano@gmail.com", "pablocoz", encodedPassword2, false,
                        currentDate, currentDate);

                userRepository.saveAll(Arrays.asList(user0, user1));
            }
            ;

            if (user_rolesRepository.count() == 0) {
                User_roles roleUser1 = new User_roles(1L, 1L);
                User_roles roleUser2 = new User_roles(2L, 1L);
                User_roles roleAdmin = new User_roles(1L, 2L);

                user_rolesRepository.saveAll(Arrays.asList(roleUser1, roleUser2, roleAdmin));
            }
            ;

            if (boardRepository.count() == 0) {
                Board board1 = new Board("TFG", new Date(0), new Date(0));
                Board board2 = new Board("Viewnext", new Date(0), new Date(0));
                boardRepository.saveAll(Arrays.asList(board1, board2));
            }
            ;

            if (user_boardRepository.count() == 0) {
                User_board user_board1 = new User_board(1L, 1L, true, 0, 1);
                User_board user_board2 = new User_board(2L, 1L, false, 0, 1);
                User_board user_board3 = new User_board(1L, 2L, true, 0, 2);
                user_boardRepository.saveAll(Arrays.asList(user_board1, user_board2, user_board3));
            }
            ;

            if (statusRepository.count() == 0) {
                Board board = boardRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Board no encontrado"));
                Status pending = new Status("Pendiente", new Date(0), new Date(0), board);
                Status inProgress = new Status("En Progreso", new Date(0), new Date(0), board);
                Status finished = new Status("Terminado", new Date(0), new Date(0), board);
                Status blocked = new Status("Bloqueado", new Date(0), new Date(0), board);

                statusRepository.saveAll(Arrays.asList(pending, inProgress, finished, blocked));
            }
            ;

            if (cardRepository.count() == 0) {
                Board board = boardRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Board no encontrado"));
                Status status1 = statusRepository.findById(1L)
                        .orElseThrow(() -> new RuntimeException("Status no encontrado"));
                Status status2 = statusRepository.findById(2L)
                        .orElseThrow(() -> new RuntimeException("Status no encontrado"));
                Card card1 = new Card("Frontend", "Crear las pantallas", new Date(0), new Date(0), 1L, new Date(0),
                        new Date(0), board, status1);
                Card card2 = new Card("Backend", "Crear los servicios", new Date(0), new Date(0), 1L, new Date(0),
                        new Date(0), board, status2);
                cardRepository.saveAll(Arrays.asList(card1, card2));

            }
            ;

            if (user_cardRepository.count() == 0) {
                User_card user_card1 = new User_card(1L, 1L);
                User_card user_card2 = new User_card(1L, 2L);
                user_cardRepository.saveAll(Arrays.asList(user_card1, user_card2));
            }
            ;
        };
    }
}