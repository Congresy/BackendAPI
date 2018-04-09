package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Pattern;

public class SocialNetwork {


    @Pattern(regexp = "^(TWITTER|INSTAGRAM|FACEBOOK|LINKEDIN|GOOGLE)$")
    private String name;
    @URL
    private String url;

    public SocialNetwork(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public SocialNetwork() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
