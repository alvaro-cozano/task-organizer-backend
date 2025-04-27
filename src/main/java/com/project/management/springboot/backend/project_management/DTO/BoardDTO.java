package com.project.management.springboot.backend.project_management.DTO;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public class BoardDTO {

    private Long id;

    @NotEmpty
    private String boardName;

    private List<UserReferenceDTO> users;

    private UserBoardReferenceDTO userBoardReference;

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

    public UserBoardReferenceDTO getUserBoardReference() {
        return userBoardReference;
    }

    public void setUserBoardReference(UserBoardReferenceDTO userBoardReference) {
        this.userBoardReference = userBoardReference;
    }

    public List<UserReferenceDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserReferenceDTO> users) {
        this.users = users;
    }
}
