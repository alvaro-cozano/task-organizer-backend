package com.project.management.springboot.backend.project_management.repositories.connection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.connection.UserBoardId;
import com.project.management.springboot.backend.project_management.entities.connection.User_board;

import org.springframework.transaction.annotation.Transactional;

public interface User_boardRepository extends CrudRepository<User_board, UserBoardId> {

    void deleteByBoardId(Long id);

    void deleteByUser_IdAndBoard_Id(Long user_id, Long board_id);

    Optional<User_board> findByUserIdAndBoardId(Long id, Long id2);

    Optional<User_board> findByBoardIdAndUserId(Long id, Long userId);

    User_board[] findByBoardId(Long id);

    List<User_board> findByUserId(Long userId);

    boolean existsByUser_idAndBoard_id(Long userId, Long boardId);

    boolean existsByUser_idAndBoard_idAndIsAdminTrue(Long userId, Long boardId);

    @Modifying
    @Transactional
    @Query("UPDATE User_board ub SET ub.posX = :posX, ub.posY = :posY WHERE ub.user_id = :userId AND ub.board_id = :boardId")
    void updateBoardPosition(Long userId, Long boardId, Integer posX, Integer posY);
}