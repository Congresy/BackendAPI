package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Course extends Talk {

    @NotBlank
    @JsonView(Views.Default.class)
    private String requeriments;

    public Course(LocalDateTime start, String name, Integer duration, Integer allowedParticipants, String place, String requeriments) {
        super(start, name, duration, allowedParticipants, place);
        this.requeriments = requeriments;
    }

    public String getRequeriments() {
        return requeriments;
    }

    public void setRequeriments(String requeriments) {
        this.requeriments = requeriments;
    }
}
