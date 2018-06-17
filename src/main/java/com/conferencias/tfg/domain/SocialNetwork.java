package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import java.util.Objects;

@Document
public class SocialNetwork {

    @Id
    private String id;
    @NotBlank
    @Pattern(regexp = "^(Twitter|Instagram|Facebook|LinkedIn|Google)$")
    private String name;
    @URL
    @NotBlank
    private String url;

    public SocialNetwork(String id, String name, String url) {
        this.name = name;
        this.url = url;
        this.id = id;
    }

    public SocialNetwork(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public SocialNetwork() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    private String actor;

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialNetwork that = (SocialNetwork) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, url);
    }
}
