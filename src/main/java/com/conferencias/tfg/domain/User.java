package com.conferencias.tfg.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User extends Actor {

    public User(String name, String surname, String email, String phone, String photo, String nick, Long place) {
        super(name, surname, email, phone, photo, nick, place);
    }
}
