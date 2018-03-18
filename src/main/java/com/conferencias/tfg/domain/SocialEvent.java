package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class SocialEvent extends Event {

    @NotBlank
    private String type;

    public SocialEvent(LocalDateTime start, String name, Integer duration, Integer allowedParticipants, Long place, String type) {
        super(start, name, duration, allowedParticipants, place);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
