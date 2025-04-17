package com.project.management.springboot.backend.project_management.DTO;

public class ChecklistItemDTO {

    private Long id;
    private String name;
    private boolean completed;
    private Long cardId;

    public ChecklistItemDTO() {
    }

    public ChecklistItemDTO(Long id, String name, boolean completed, Long cardId) {
        this.id = id;
        this.name = name;
        this.completed = completed;
        this.cardId = cardId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}