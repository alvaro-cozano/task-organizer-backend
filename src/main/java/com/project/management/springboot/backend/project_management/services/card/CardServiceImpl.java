package com.project.management.springboot.backend.project_management.services.card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.management.springboot.backend.project_management.DTO.CardDTO;
import com.project.management.springboot.backend.project_management.DTO.UserReferenceDTO;
import com.project.management.springboot.backend.project_management.entities.connection.User_card;
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.repositories.BoardRepository;
import com.project.management.springboot.backend.project_management.repositories.CardRepository;
import com.project.management.springboot.backend.project_management.repositories.StatusRepository;
import com.project.management.springboot.backend.project_management.repositories.UserRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_boardRepository;
import com.project.management.springboot.backend.project_management.repositories.connection.User_cardRepository;
import com.project.management.springboot.backend.project_management.utils.mapper.CardMapper;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CardServiceImpl implements CardService {

    private final User_boardRepository user_boardRepository;

    private final StatusRepository statusRepository;

    private final BoardRepository boardRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private User_cardRepository userCardRepository;

    CardServiceImpl(BoardRepository boardRepository, StatusRepository statusRepository,
            User_boardRepository user_boardRepository) {
        this.boardRepository = boardRepository;
        this.statusRepository = statusRepository;
        this.user_boardRepository = user_boardRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findAll() {
        List<Card> cards = (List<Card>) cardRepository.findAll();
        return cards.stream().map(CardMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardDTO> findById(Long id) {
        return cardRepository.findById(id).map(CardMapper::toDTO);
    }

    @Override
    @Transactional
    public CardDTO save(CardDTO cardDTO) {
        List<User> users = new ArrayList<>();

        if (cardDTO.getUsers() != null) {
            for (UserReferenceDTO userDTO : cardDTO.getUsers()) {
                userRepository.findByEmail(userDTO.getEmail()).ifPresent(users::add);
            }
        }

        users = users.stream().distinct().toList();

        Card card = CardMapper.toEntity(cardDTO);
        card.setUsers(users);
        Card savedCard = cardRepository.save(card);

        for (User user : users) {
            addCardToUser(savedCard.getId(), user.getId());
        }

        return CardMapper.toDTO(savedCard);
    }

    @Override
    @Transactional
    public Optional<CardDTO> update(Long id, CardDTO cardDTO) {
        Optional<Card> optionalCard = cardRepository.findById(id);
        if (optionalCard.isEmpty())
            return Optional.empty();

        List<User> usersToAssociate = new ArrayList<>();

        if (cardDTO.getUsers() != null) {
            for (UserReferenceDTO userDTO : cardDTO.getUsers()) {
                userRepository.findByEmail(userDTO.getEmail()).ifPresent(usersToAssociate::add);
            }
        }

        usersToAssociate = usersToAssociate.stream().distinct().collect(Collectors.toList());

        Card card = optionalCard.get();
        card.setCardTitle(cardDTO.getCardTitle()); // Actualizar el título
        card.setDescription(cardDTO.getDescription()); // Actualizar la descripción
        card.setStart_date(cardDTO.getStartDate()); // Actualizar la fecha de inicio
        card.setEnd_date(cardDTO.getEndDate()); // Actualizar la fecha de fin
        card.setPriority(cardDTO.getPriority()); // Actualizar la prioridad
        card.setBoard(boardRepository.findById(cardDTO.getBoard_id()).orElse(null)); // Actualizar el tablero
        card.setStatus(statusRepository.findById(cardDTO.getStatus_id()).orElse(null)); // Actualizar el estado
        card.setUsers(usersToAssociate); // Actualizar los usuarios

        Card savedCard = cardRepository.save(card);

        // Eliminar relaciones que ya no deberían existir
        List<User_card> existingRelations = userCardRepository.findByCardId(savedCard.getId());
        for (User_card existing : existingRelations) {
            if (usersToAssociate.stream().noneMatch(u -> u.getId().equals(existing.getUser_id()))) {
                userCardRepository.delete(existing);
            }
        }

        // Crear nuevas relaciones si no existen
        for (User user : usersToAssociate) {
            Optional<User_card> existing = userCardRepository.findByUserIdAndCardId(user.getId(), savedCard.getId());
            if (existing.isEmpty()) {
                addCardToUser(user.getId(), savedCard.getId());
            }
        }

        return Optional.of(CardMapper.toDTO(savedCard));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tarjeta no encontrada."));

        // Eliminar relaciones en la tabla intermedia
        userCardRepository.deleteByCardId(card.getId());

        // Eliminar la tarjeta
        cardRepository.delete(card);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCardTitle(String cardTitle) {
        return cardRepository.existsByCardTitle(cardTitle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findCardsByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario actual no encontrado");
        }

        User currentUser = optionalUser.get();

        List<User_card> userCards = userCardRepository.findByUserId(currentUser.getId());

        List<Long> cardIds = userCards.stream()
                .map(User_card::getCard_id)
                .collect(Collectors.toList());

        Iterable<Card> cards = cardRepository.findAllById(cardIds);
        List<CardDTO> cardDTOs = new ArrayList<>();
        cards.forEach(card -> cardDTOs.add(CardMapper.toDTO(card)));

        return cardDTOs;
    }

    @Override
    public List<CardDTO> getCardsForBoard(Long board_id, Long user_id) {
        // Verificar si el usuario está asociado al tablero
        if (!user_boardRepository.existsByUser_idAndBoard_id(user_id, board_id)) {
            throw new AccessDeniedException("El usuario no está autorizado para ver las tarjetas de este tablero.");
        }

        // Obtener todas las tarjetas asociadas al tablero
        List<Card> cards = cardRepository.findByBoardId(board_id);

        // Convertir las tarjetas a DTO
        return cards.stream()
                .map(CardMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addCardToUser(Long card_id, Long user_id) {
        // Verificamos si la relación ya existe
        Optional<User_card> existingUserCard = userCardRepository.findByCardIdAndUserId(user_id, card_id);
        if (existingUserCard.isEmpty()) {
            // Si no existe, creamos la nueva relación
            User_card userCard = new User_card(user_id, card_id);
            userCardRepository.save(userCard);
        } // Si la relación ya existe, no hacemos nada
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> getCardsByBoardAndStatus(Long boardId, Long statusId, Long userId) {
        // Obtener todas las tarjetas del tablero con ese status
        List<Card> cardsByBoardAndStatus = cardRepository.findByBoardIdAndStatusId(boardId, statusId);

        // Filtrar las que están asociadas al usuario autenticado
        return cardsByBoardAndStatus.stream()
                .map(CardMapper::toDTO)
                .collect(Collectors.toList());
    }

}
