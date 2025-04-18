package com.project.management.springboot.backend.project_management.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.models.Card;

public interface CardRepository extends CrudRepository<Card, Long> {
    boolean existsByCardTitle(String cardTitle);

    boolean existsById(Long id);

    List<Card> findByBoardId(Long boardId);

    List<Card> findByBoardIdAndStatusId(Long boardId, Long statusId);
}
