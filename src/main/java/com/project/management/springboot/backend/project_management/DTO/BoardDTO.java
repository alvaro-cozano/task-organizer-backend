package com.project.management.springboot.backend.project_management.DTO;

import java.util.ArrayList;
import java.util.List;

public class BoardDTO {

    private Long id;
    private String boardName;
    private List<UserReferenceDTO> users; // Lista de usuarios asociados al tablero

    public BoardDTO() {
        users = new ArrayList<>();
    }

    public BoardDTO(Long id, String boardName, List<UserReferenceDTO> users) {
        this.id = id;
        this.boardName = boardName;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public List<UserReferenceDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserReferenceDTO> users) {
        this.users = users;
    }
}
