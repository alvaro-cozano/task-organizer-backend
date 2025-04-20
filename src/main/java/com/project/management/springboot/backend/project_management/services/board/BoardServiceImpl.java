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
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.BoardRepository;
import com.project.management.springboot.backend.project_management.repositories.CardRepository;
import com.project.management.springboot.backend.project_management.repositories.StatusRepository;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_boardRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_cardRepository;
import com.project.management.springboot.backend.project_management.utils.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private User_boardRepository userBoardRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private User_cardRepository userCardRepository;

    @Autowired
    private StatusRepository statusRepository;

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

        // Validar si ya existe un tablero con ese nombre para este usuario
        List<User_board> userBoards = userBoardRepository.findByUserId(currentUser.getId());
        for (User_board ub : userBoards) {
            Optional<Board> board = repository.findById(ub.getBoard_id());
            if (board.isPresent() && board.get().getBoardName().equalsIgnoreCase(boardDTO.getBoardName())) {
                throw new RuntimeException("Ya existe un tablero con ese nombre para este usuario.");
            }
        }

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
        User currentUser = optionalCurrentUser.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el tablero por su ID
        Optional<Board> boardOptional = repository.findById(id);
        boardOptional.ifPresent(boardDb -> {
            // Verificar si el usuario actual es administrador en el tablero
            Optional<User_board> userBoardOptional = userBoardRepository.findByUserIdAndBoardId(currentUser.getId(),
                    id);
            if (userBoardOptional.isPresent() && userBoardOptional.get().getIsAdmin()) {
                // Si el usuario es administrador

                // Eliminar las relaciones de tarjetas con los usuarios (user_card)
                List<Long> cardIds = cardRepository.findByBoardId(id).stream()
                        .map(Card::getId)
                        .collect(Collectors.toList());
                if (!cardIds.isEmpty()) {
                    // Eliminar las relaciones entre usuarios y tarjetas asociadas al tablero
                    userCardRepository.deleteByCardIdIn(cardIds); // Utilizamos el método deleteByCardIdIn para eliminar
                                                                  // las relaciones
                }

                // Eliminar las tarjetas asociadas al tablero
                cardRepository.deleteByBoardId(id); // Eliminar las tarjetas asociadas al tablero

                // Eliminar las relaciones en la tabla intermedia entre usuarios y tableros
                userBoardRepository.deleteByBoardId(id); // Eliminar las relaciones en la tabla intermedia (user_board)

                // Eliminar los estados de las tarjetas asociadas al tablero, si es necesario
                statusRepository.deleteByBoardId(id); // Eliminar los estados asociados al tablero

                // Finalmente, eliminar el tablero
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

        Optional<User> optionalCurrentUser = userRepository.findByUsername(username);
        if (optionalCurrentUser.isEmpty()) {
            return Optional.empty();
        }

        User currentUser = optionalCurrentUser.get();

        Optional<User_board> userBoardRelation = userBoardRepository.findByUserIdAndBoardId(currentUser.getId(), id);
        if (userBoardRelation.isEmpty() || !userBoardRelation.get().getIsAdmin()) {
            return Optional.empty();
        }

        // Validar si el usuario ya tiene otro tablero con ese nombre
        List<User_board> userBoards = userBoardRepository.findByUserId(currentUser.getId());
        for (User_board ub : userBoards) {
            if (!ub.getBoard_id().equals(id)) { // Excluir el actual
                Optional<Board> board = repository.findById(ub.getBoard_id());
                if (board.isPresent() && board.get().getBoardName().equalsIgnoreCase(boardDTO.getBoardName())) {
                    throw new RuntimeException("Ya existe otro tablero con ese nombre para este usuario.");
                }
            }
        }

        List<User> usersToAssociate = new ArrayList<>();
        usersToAssociate.add(currentUser);

        if (boardDTO.getUsers() != null) {
            for (UserReferenceDTO userDTO : boardDTO.getUsers()) {
                Optional<User> userFromDb = userRepository.findByEmail(userDTO.getEmail());
                userFromDb.ifPresent(usersToAssociate::add);
            }
        }

        usersToAssociate = usersToAssociate.stream().distinct().collect(Collectors.toList());

        Optional<Board> optionalBoard = repository.findById(id);
        if (optionalBoard.isEmpty()) {
            return Optional.empty();
        }

        Board existingBoard = optionalBoard.get();
        existingBoard.setBoardName(boardDTO.getBoardName());
        existingBoard.setUsers(usersToAssociate);

        Board savedBoard = repository.save(existingBoard);

        for (User_board existingRelation : userBoardRepository.findByBoardId(savedBoard.getId())) {
            if (!usersToAssociate.stream().anyMatch(u -> u.getId().equals(existingRelation.getUser_id()))) {
                userBoardRepository.delete(existingRelation);
            }
        }

        for (User user : usersToAssociate) {
            boolean isAdmin = user.getId().equals(currentUser.getId());
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
}
