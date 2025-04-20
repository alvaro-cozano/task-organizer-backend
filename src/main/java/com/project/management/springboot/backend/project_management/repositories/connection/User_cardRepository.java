package com.project.management.springboot.backend.project_management.repositories.connection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.project.management.springboot.backend.project_management.entities.connection.UserCardId;
import com.project.management.springboot.backend.project_management.entities.connection.User_card;
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.User;

import jakarta.transaction.Transactional;

public interface User_cardRepository extends CrudRepository<User_card, UserCardId> {

    void deleteByUser_idAndCard_id(Long user_id, Long card_id);

    Optional<User_card> findByUserIdAndCardId(Long user_id, Long card_id);

    Optional<User_card> findByCardIdAndUserId(Long user_id, Long card_id);

    List<User_card> findByCardId(Long card_id);

    List<User_card> findByUserId(Long user_id);

    void deleteByCard(Card card);

    @Transactional
    @Modifying
    @Query("DELETE FROM User_card uc WHERE uc.card.id = :cardId")
    void deleteByCardId(@Param("cardId") Long cardId);

    Optional<User_card> findByUserAndCard(User user, Card card);;

    // Eliminar relaciones por tablero
    @Transactional
    @Modifying
    @Query("DELETE FROM User_card uc WHERE uc.card.board.id = :boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);

    // Eliminar relaciones por lista de IDs de tarjetas
    @Transactional
    @Modifying
    @Query("DELETE FROM User_card uc WHERE uc.card.id IN :cardIds")
    void deleteByCardIdIn(@Param("cardIds") List<Long> cardIds);
}
