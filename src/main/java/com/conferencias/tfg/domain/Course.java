package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.NotBlank;

public class Course extends Talk {

    @NotBlank
    private String requeriments;

}
