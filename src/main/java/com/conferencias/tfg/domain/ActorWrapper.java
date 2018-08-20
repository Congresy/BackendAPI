package com.conferencias.tfg.domain;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.UserAccount;

import java.util.Objects;

public class ActorWrapper {

    private Actor actor;
    private UserAccount userAccount;

    public ActorWrapper() {

    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorWrapper that = (ActorWrapper) o;
        return Objects.equals(actor, that.actor) &&
                Objects.equals(userAccount, that.userAccount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actor, userAccount);
    }
}
