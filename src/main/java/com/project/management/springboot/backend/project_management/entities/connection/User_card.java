package com.project.management.springboot.backend.project_management.entities.connection;

import com.project.management.springboot.backend.project_management.entities.models.User;
import com.project.management.springboot.backend.project_management.entities.models.Card;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@IdClass(UserCardId.class)
@Table(name = "user_card")
public class User_card {

    @Id
    private Long user_id;

    @Id
    private Long card_id;

    // Relación con User
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    // Relación con Card
    @ManyToOne
    @JoinColumn(name = "card_id", insertable = false, updatable = false)
    private Card card;

    public User_card() {
    }

    public User_card(Long user_id, Long card_id) {
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

    public User getUser() {
        return user;
    }

    public Card getCard() {
        return card;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
        result = prime * result + ((card_id == null) ? 0 : card_id.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((card == null) ? 0 : card.hashCode());
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
        User_card other = (User_card) obj;
        if (user_id == null) {
            if (other.user_id != null)
                return false;
        } else if (!user_id.equals(other.user_id))
            return false;
        if (card_id == null) {
            if (other.card_id != null)
                return false;
        } else if (!card_id.equals(other.card_id))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (card == null) {
            if (other.card != null)
                return false;
        } else if (!card.equals(other.card))
            return false;
        return true;
    }

}
