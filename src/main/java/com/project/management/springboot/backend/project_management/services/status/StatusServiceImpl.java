package com.project.management.springboot.backend.project_management.services.status;

import com.project.management.springboot.backend.project_management.DTO.StatusDTO;
import com.project.management.springboot.backend.project_management.entities.models.Board;
import com.project.management.springboot.backend.project_management.entities.models.Status;
import com.project.management.springboot.backend.project_management.repositories.StatusRepository;
import com.project.management.springboot.backend.project_management.utils.mapper.StatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<StatusDTO> getStatusesByBoard(Long boardId) {
        return statusRepository.findByBoardId(boardId)
                .stream()
                .map(StatusMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StatusDTO createStatus(StatusDTO statusDTO) {
        Status status = StatusMapper.toEntity(statusDTO);

        if (statusDTO.getBoardId() != null) {
            Board board = new Board();
            board.setId(statusDTO.getBoardId());
            status.setBoard(board);
        }

        Status saved = statusRepository.save(status);
        return StatusMapper.toDTO(saved);
    }

    @Override
    public StatusDTO updateStatus(Long statusId, StatusDTO statusDTO) {
        Optional<Status> existingOpt = statusRepository.findById(statusId);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Status not found");
        }

        Status existing = existingOpt.get();
        existing.setName(statusDTO.getName());

        if (statusDTO.getBoardId() != null) {
            Board board = new Board();
            board.setId(statusDTO.getBoardId());
            existing.setBoard(board);
        }

        Status updated = statusRepository.save(existing);
        return StatusMapper.toDTO(updated);
    }

    @Override
    public void deleteStatus(Long statusId) {
        if (!statusRepository.existsById(statusId)) {
            throw new IllegalArgumentException("Status not found");
        }
        statusRepository.deleteById(statusId);
    }
}
