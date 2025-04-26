package com.project.management.springboot.backend.project_management.entities.connection;

import com.project.management.springboot.backend.project_management.entities.models.Board;
import com.project.management.springboot.backend.project_management.entities.models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@IdClass(UserBoardId.class)
@Table(name = "user_board")
public class User_board {

    @Id
    private Long user_id;

    @Id
    private Long board_id;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column(nullable = true)
    private Integer posX;

    @Column(nullable = true)
    private Integer posY;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Relación con la entidad Board
    @ManyToOne
    @JoinColumn(name = "board_id", insertable = false, updatable = false)
    private Board board;

    public User_board() {
    }

    public User_board(Long user_id, Long board_id, boolean isAdmin) {
        this.user_id = user_id;
        this.board_id = board_id;
        this.isAdmin = isAdmin;
    }

    public User_board(Long user_id, Long board_id, boolean isAdmin, Integer posX, Integer posY) {
        this.user_id = user_id;
        this.board_id = board_id;
        this.isAdmin = isAdmin;
        this.posX = posX;
        this.posY = posY;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getBoard_id() {
        return board_id;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setBoard_id(Long board_id) {
        this.board_id = board_id;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
        result = prime * result + ((board_id == null) ? 0 : board_id.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((board == null) ? 0 : board.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User_board other = (User_board) obj;
        if (user_id == null) {
            if (other.user_id != null)
                return false;
        } else if (!user_id.equals(other.user_id))
            return false;
        if (board_id == null) {
            if (other.board_id != null)
                return false;
        } else if (!board_id.equals(other.board_id))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (board == null) {
            if (other.board != null)
                return false;
        } else if (!board.equals(other.board))
            return false;
        return true;
    }

}
