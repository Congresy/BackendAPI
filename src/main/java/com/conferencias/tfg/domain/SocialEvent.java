package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.NotBlank;

public class SocialEvent extends Event {

    @NotBlank
    private String type;

}
