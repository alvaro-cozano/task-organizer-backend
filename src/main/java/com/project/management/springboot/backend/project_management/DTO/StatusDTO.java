package com.project.management.springboot.backend.project_management.DTO;

public class StatusDTO {

    private Long id;
    private String name;
    private Long boardId;

    public StatusDTO() {
    }

    public StatusDTO(Long id, String name, Long boardId) {
        this.id = id;
        this.name = name;
        this.boardId = boardId;
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

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
}
