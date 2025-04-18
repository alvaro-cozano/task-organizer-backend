package com.project.management.springboot.backend.project_management.repositories;

import com.project.management.springboot.backend.project_management.entities.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByNameIgnoreCase(String name);

    List<Status> findByBoardId(Long boardId);
}
