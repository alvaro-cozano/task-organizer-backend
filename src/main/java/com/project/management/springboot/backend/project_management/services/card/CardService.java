package com.project.management.springboot.backend.project_management.services.card;

import java.util.List;
import java.util.Optional;

import com.project.management.springboot.backend.project_management.DTO.CardDTO;

public interface CardService {
    List<CardDTO> findAll();

    Optional<CardDTO> findById(Long id);

    CardDTO save(CardDTO CardDTO);

    Optional<CardDTO> update(Long id, CardDTO CardDTO);

    void delete(Long id);

    boolean existsByCardTitle(String cardTitle);

    List<CardDTO> findCardsByCurrentUser();

    void addCardToUser(Long card_id, Long user_id);

    List<CardDTO> getCardsForBoard(Long board_id, Long user_id);

    List<CardDTO> getCardsByBoardAndStatus(Long boardId, Long statusId, Long userId);
}
