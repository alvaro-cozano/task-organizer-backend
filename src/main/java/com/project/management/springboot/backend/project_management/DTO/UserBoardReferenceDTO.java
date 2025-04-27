package com.project.management.springboot.backend.project_management.DTO;

public class UserBoardReferenceDTO {
    private Integer posX;
    private Integer posY;

    public UserBoardReferenceDTO() {
    }

    public UserBoardReferenceDTO(Integer posX, Integer posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }
}
