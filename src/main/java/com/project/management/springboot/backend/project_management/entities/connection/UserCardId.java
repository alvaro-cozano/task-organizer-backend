package com.project.management.springboot.backend.project_management.entities.connection;

import java.io.Serializable;
import java.util.Objects;

public class UserCardId implements Serializable {

    private Long user_id;
    private Long card_id;

    public UserCardId() {
    }

    public UserCardId(Long user_id, Long card_id) {
        this.user_id = user_id;
        this.card_id = card_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getCard_id() {
        return card_id;
    }

    public void setCard_id(Long card_id) {
        this.card_id = card_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserCardId))
            return false;
        UserCardId that = (UserCardId) o;
        return Objects.equals(user_id, that.user_id) && Objects.equals(card_id, that.card_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, card_id);
    }
}
