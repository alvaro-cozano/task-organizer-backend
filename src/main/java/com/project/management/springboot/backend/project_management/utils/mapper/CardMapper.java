package com.project.management.springboot.backend.project_management.utils.mapper;

import com.project.management.springboot.backend.project_management.DTO.CardDTO;
import com.project.management.springboot.backend.project_management.entities.models.Board;
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.Status;

import java.util.Date;

public class CardMapper {

    public static CardDTO toDTO(Card card) {
        if (card == null) {
            return null;
        }

        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDesciption(),
                card.getStart_date(),
                card.getEnd_date(),
                card.getPriority(),
                card.getBoard() != null ? card.getBoard().getId() : null,
                card.getStatus() != null ? card.getStatus().getId() : null);
    }

    public static Card toEntity(CardDTO dto) {
        if (dto == null) {
            return null;
        }

        Card card = new Card(
                dto.getTitle(),
                dto.getDesciption(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getPriority(),
                new Date(), // createdAt
                new Date() // updatedAt
        );

        if (dto.getBoardId() != null) {
            Board board = new Board();
            board.setId(dto.getBoardId());
            card.setBoard(board);
        }

        if (dto.getStatusId() != null) {
            Status status = new Status();
            status.setId(dto.getStatusId());
            card.setStatus(status);
        }

        return card;
    }
}
