package com.conferencias.tfg.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Regular extends Talk {

    public Regular(LocalDateTime start, String name, Integer duration, Integer allowedParticipants, Long place) {
        super(start, name, duration, allowedParticipants, place);
    }
}
