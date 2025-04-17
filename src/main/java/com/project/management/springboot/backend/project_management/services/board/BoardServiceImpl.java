package com.project.management.springboot.backend.project_management.services.board;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.management.springboot.backend.project_management.DTO.BoardDTO;
import com.project.management.springboot.backend.project_management.DTO.UserReferenceDTO;
import com.project.management.springboot.backend.project_management.entities.connection.User_board;
import com.project.management.springboot.backend.project_management.entities.models.Board;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.BoardRepository;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_boardRepository;
import com.project.management.springboot.backend.project_management.utils.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private User_boardRepository userBoardRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BoardDTO> findAll() {
        List<Board> boards = (List<Board>) repository.findAll();
        return boards.stream().map(BoardMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BoardDTO> findById(Long id) {
        Optional<Board> boardOptional = repository.findById(id);
        return boardOptional.map(BoardMapper::toDTO);
    }

    @Override
    @Transactional
    public BoardDTO save(BoardDTO boardDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalCurrentUser = userRepository.findByUsername(username);
        if (optionalCurrentUser.isEmpty()) {
            throw new RuntimeException("Usuario actual no encontrado");
        }

        User currentUser = optionalCurrentUser.get();
        List<User> users = new ArrayList<>();
        users.add(currentUser);

        if (boardDTO.getUsers() != null) {
            for (UserReferenceDTO userDTO : boardDTO.getUsers()) {
                Optional<User> userFromDb = userRepository.findByEmail(userDTO.getEmail());
                userFromDb.ifPresent(users::add);
            }
        }

        users = users.stream().distinct().toList();

        Board board = BoardMapper.toEntity(boardDTO);
        board.setUsers(users);

        Board savedBoard = repository.save(board);

        for (User user : users) {
            boolean isAdmin = user.getId().equals(currentUser.getId());
            User_board userBoard = new User_board(user.getId(), savedBoard.getId(), isAdmin);
            userBoardRepository.save(userBoard);
        }

        return BoardMapper.toDTO(savedBoard);
    }

    @Override
    @Transactional
    public Optional<BoardDTO> delete(Long id) {
        // Obtener el usuario actual desde el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Buscar el usuario en la base de datos por nombre de usuario
        Optional<User> optionalCurrentUser = userRepository.findByUsername(username);
        User currentUser = optionalCurrentUser.orElseThrow(); // Obtiene el usuario actual

        // Buscar el tablero por su ID
        Optional<Board> boardOptional = repository.findById(id);
        boardOptional.ifPresent(boardDb -> {
            // Verificar si el usuario actual es administrador en el tablero
            Optional<User_board> userBoardOptional = userBoardRepository.findByUserIdAndBoardId(currentUser.getId(),
                    id);
            if (userBoardOptional.isPresent() && userBoardOptional.get().getIsAdmin()) {
                // Si el usuario es administrador, eliminamos la relación y el tablero
                userBoardRepository.deleteByBoardId(id); // Eliminar las relaciones en la tabla intermedia
                repository.delete(boardDb); // Eliminar el tablero
            } else {
                throw new RuntimeException("No tienes permiso para eliminar este tablero"); // Lanzar excepción si no es
                                                                                            // admin
            }
        });

        // Retornar el DTO del tablero eliminado
        return boardOptional.map(BoardMapper::toDTO);
    }

    @Override
    @Transactional
    public Optional<BoardDTO> update(Long id, BoardDTO boardDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Buscar el usuario actual
        Optional<User> optionalCurrentUser = userRepository.findByUsername(username);
        if (optionalCurrentUser.isEmpty()) {
            return Optional.empty();
        }

        User currentUser = optionalCurrentUser.get();

        // Verificar si el usuario es admin del tablero
        Optional<User_board> userBoardRelation = userBoardRepository.findByUserIdAndBoardId(currentUser.getId(), id);
        if (userBoardRelation.isEmpty() || !userBoardRelation.get().getIsAdmin()) {
            return Optional.empty();
        }

        // Obtener usuarios a asociar (desde DTOs)
        List<User> usersToAssociate = new ArrayList<>();
        usersToAssociate.add(currentUser); // Siempre incluir al usuario actual

        if (boardDTO.getUsers() != null) {
            for (UserReferenceDTO userDTO : boardDTO.getUsers()) {
                Optional<User> userFromDb = userRepository.findByEmail(userDTO.getEmail());
                userFromDb.ifPresent(usersToAssociate::add);
            }
        }

        // Eliminar duplicados
        usersToAssociate = usersToAssociate.stream().distinct().collect(Collectors.toList());

        // Verificar existencia del tablero
        Optional<Board> optionalBoard = repository.findById(id);
        if (optionalBoard.isEmpty()) {
            return Optional.empty();
        }

        Board existingBoard = optionalBoard.get();
        existingBoard.setBoardName(boardDTO.getBoardName());
        existingBoard.setUsers(usersToAssociate);

        // Guardar el tablero actualizado
        Board savedBoard = repository.save(existingBoard);

        // Eliminar relaciones de usuarios que ya no están en la lista
        for (User_board existingRelation : userBoardRepository.findByBoardId(savedBoard.getId())) {
            if (!usersToAssociate.stream().anyMatch(u -> u.getId().equals(existingRelation.getUser_id()))) {
                userBoardRepository.delete(existingRelation);
            }
        }

        // Crear nuevas relaciones en la tabla intermedia
        for (User user : usersToAssociate) {
            boolean isAdmin = user.getId().equals(currentUser.getId()); // Solo el actual es admin

            // Verificar si ya existe la relación antes de agregarla
            Optional<User_board> existingRelation = userBoardRepository.findByUserIdAndBoardId(user.getId(),
                    savedBoard.getId());
            if (existingRelation.isEmpty()) {
                User_board userBoard = new User_board(user.getId(), savedBoard.getId(), isAdmin);
                userBoardRepository.save(userBoard);
            }
        }

        return Optional.of(BoardMapper.toDTO(savedBoard));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardDTO> findBoardsByCurrentUser() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalCurrentUser = userRepository.findByUsername(username);
        if (optionalCurrentUser.isEmpty()) {
            throw new RuntimeException("Usuario actual no encontrado");
        }

        User currentUser = optionalCurrentUser.get();

        // Obtener relaciones User_board por ID del usuario
        List<User_board> userBoards = userBoardRepository.findByUserId(currentUser.getId());

        // Extraer los IDs de los tableros
        List<Long> boardIds = userBoards.stream()
                .map(User_board::getBoard_id)
                .collect(Collectors.toList());

        // Buscar los tableros por sus IDs
        Iterable<Board> iterableBoards = repository.findAllById(boardIds);
        List<Board> boards = new ArrayList<>();
        iterableBoards.forEach(boards::add);

        return boards.stream()
                .map(BoardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByBoardName(String boardName) {
        return repository.existsByBoardName(boardName);
    }
}
