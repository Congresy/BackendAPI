package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Pattern;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialNetwork that = (SocialNetwork) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url);
    }
}
