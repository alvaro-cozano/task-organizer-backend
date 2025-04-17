package com.project.management.springboot.backend.project_management.utils.mapper;

import java.util.Date;

import com.project.management.springboot.backend.project_management.DTO.ChecklistItemDTO;
import com.project.management.springboot.backend.project_management.entities.models.Card;
import com.project.management.springboot.backend.project_management.entities.models.Checklist_item;

public class ChecklistItemMapper {
    public Checklist_item toEntity(ChecklistItemDTO dto) {
        Checklist_item item = new Checklist_item(
                dto.getName(),
                dto.isCompleted(),
                new Date(),
                new Date());

        if (dto.getCardId() != null) {
            Card card = new Card();
            card.setId(dto.getCardId());
            item.setCard(card);
        }

        return item;
    }

    public ChecklistItemDTO toDTO(Checklist_item item) {
        return new ChecklistItemDTO(
                item.getId(),
                item.getName(),
                item.isCompleted(),
                item.getCard() != null ? item.getCard().getId() : null);
    }
}
