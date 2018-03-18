package com.conferencias.tfg.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Guest extends Talk {

    public Guest(LocalDateTime start, String name, Integer duration, Integer allowedParticipants, String place) {
        super(start, name, duration, allowedParticipants, place);
    }
}
