package com.project.management.springboot.backend.project_management.utils.mapper;

import com.project.management.springboot.backend.project_management.DTO.CardDTO;
import com.project.management.springboot.backend.project_management.DTO.UserReferenceDTO;
import com.project.management.springboot.backend.project_management.entities.models.Board;
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.Status;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CardMapper {

    // Convertir Card a CardDTO
    public static CardDTO toDTO(Card card) {
        if (card == null) {
            return null;
        }

        List<UserReferenceDTO> userDTOs = card.getUsers() != null
                ? card.getUsers().stream()
                        .map(user -> new UserReferenceDTO(user.getEmail()))
                        .collect(Collectors.toList())
                : null;

        return new CardDTO(
                card.getId(),
                card.getCardTitle(),
                card.getDescription(),
                card.getStart_date(),
                card.getEnd_date(),
                card.getPriority(),
                card.getBoard() != null ? card.getBoard().getId() : null,
                card.getStatus() != null ? card.getStatus().getId() : null,
                userDTOs);
    }

    // Convertir CardDTO a Card
    public static Card toEntity(CardDTO dto) {
        if (dto == null) {
            return null;
        }

        Card card = new Card(
                dto.getCardTitle(),
                dto.getDescription(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getPriority(),
                new Date(), // createdAt
                new Date(),
                null,
                null // updatedAt

        );

        if (dto.getBoard_id() != null) {
            Board board = new Board();
            board.setId(dto.getBoard_id());
            card.setBoard(board);
        }

        if (dto.getStatus_id() != null) {
            Status status = new Status();
            status.setId(dto.getStatus_id());
            card.setStatus(status);
        }

        // La asignación de usuarios se hace desde el servicio

        return card;
    }
}
