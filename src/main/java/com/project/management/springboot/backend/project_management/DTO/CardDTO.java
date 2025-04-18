package com.project.management.springboot.backend.project_management.DTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CardDTO {

    private Long id;

    @NotEmpty
    private String cardTitle;
    private String description;

    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date endDate;

    @NotNull
    private Long priority;

    private Long board_id;
    private Long status_id;
    private List<UserReferenceDTO> users;

    public CardDTO() {
        users = new ArrayList<>();
    }

    public CardDTO(
            Long id,
            String cardTitle,
            String description,
            Date startDate,
            Date endDate,
            Long priority,
            Long board_id,
            Long status_id,
            List<UserReferenceDTO> users) {
        this.id = id;
        this.cardTitle = cardTitle;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.board_id = board_id;
        this.status_id = status_id;
        this.users = users;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getBoard_id() {
        return board_id;
    }

    public void setBoard_id(Long board_id) {
        this.board_id = board_id;
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void settatus_id(Long status_id) {
        this.status_id = status_id;
    }

    public List<UserReferenceDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserReferenceDTO> users) {
        this.users = users;
    }
}