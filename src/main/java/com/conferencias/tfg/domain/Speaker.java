package com.conferencias.tfg.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Speaker extends User {

    public Speaker(String name, String surname, String email, String phone, String photo, String nick, String place) {
        super(name, surname, email, phone, photo, nick, place);
    }
}
