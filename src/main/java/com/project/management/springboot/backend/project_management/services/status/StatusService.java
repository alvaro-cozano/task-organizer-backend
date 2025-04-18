package com.project.management.springboot.backend.project_management.services.status;

import java.util.List;

import com.project.management.springboot.backend.project_management.DTO.StatusDTO;

public interface StatusService {
    List<StatusDTO> getStatusesByBoard(Long boardId);

    StatusDTO createStatus(StatusDTO statusDTO);

    StatusDTO updateStatus(Long statusId, StatusDTO statusDTO);

    void deleteStatus(Long statusId);
}
