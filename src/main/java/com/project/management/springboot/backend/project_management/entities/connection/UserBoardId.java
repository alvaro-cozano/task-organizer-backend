package com.project.management.springboot.backend.project_management.entities.connection;

import java.io.Serializable;
import java.util.Objects;

public class UserBoardId implements Serializable {

    private Long user_id;
    private Long board_id;

    public UserBoardId() {
    }

    public UserBoardId(Long user_id, Long board_id) {
        this.user_id = user_id;
        this.board_id = board_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserBoardId))
            return false;
        UserBoardId that = (UserBoardId) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(board_id, that.board_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, board_id);
    }
}