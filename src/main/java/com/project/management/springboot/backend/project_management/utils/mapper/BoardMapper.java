package com.project.management.springboot.backend.project_management.utils.mapper;

import com.project.management.springboot.backend.project_management.DTO.BoardDTO;
import com.project.management.springboot.backend.project_management.DTO.UserReferenceDTO;
import com.project.management.springboot.backend.project_management.entities.models.Board;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BoardMapper {

    // Convertir Board a BoardDTO
    public static BoardDTO toDTO(Board board) {
        if (board == null) {
            return null;
        }

        List<UserReferenceDTO> userDTOs = board.getUsers() != null
                ? board.getUsers().stream()
                        .map(user -> new UserReferenceDTO(user.getEmail()))
                        .collect(Collectors.toList())
                : null;

        return new BoardDTO(
                board.getId(),
                board.getBoardName(),
                userDTOs);
    }

    // Convertir BoardDTO a Board
    public static Board toEntity(BoardDTO dto) {
        if (dto == null) {
            return null;
        }

        Board board = new Board();
        board.setId(dto.getId());
        board.setBoardName(dto.getBoardName());
        board.setCreatedAt(new Date());
        board.setUpdatedAt(new Date());

        // La asignación de usuarios queda fuera (se hace desde el servicio)
        return board;
    }
}
