package com.project.management.springboot.backend.project_management.repositories;

import org.springframework.data.repository.CrudRepository;

import com.project.management.springboot.backend.project_management.entities.models.Board;

public interface BoardRepository extends CrudRepository<Board, Long> {
    boolean existsByBoardName(String boardName);
}
