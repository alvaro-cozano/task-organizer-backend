package com.project.management.springboot.backend.project_management.DTO;

import java.util.Date;

public class CardDTO {

    private Long id;
    private String title;
    private String desciption;
    private Date startDate;
    private Date endDate;
    private Long priority;

    private Long boardId;
    private Long statusId;

    public CardDTO() {
    }

    public CardDTO(Long id, String title, String desciption, Date startDate, Date endDate, Long priority,
            Long boardId, Long statusId) {
        this.id = id;
        this.title = title;
        this.desciption = desciption;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.boardId = boardId;
        this.statusId = statusId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }
}