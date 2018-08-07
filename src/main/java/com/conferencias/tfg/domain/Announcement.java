package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
public class Announcement {

    @Id
    private String id;
    @URL
    private String picture;

    private String url;

    @NotBlank
    private String description;

    private String idConference;

    public Announcement() {

    }

    public Announcement(String id, String picture, String url, String description, String idConference) {
        this.id = id;
        this.picture = picture;
        this.url = url;
        this.description = description;
        this.idConference = idConference;
    }

    public Announcement(String picture, String url, String description, String idConference) {
        this.picture = picture;
        this.url = url;
        this.description = description;
        this.idConference = idConference;
    }

    public String getIdConference() {
        return idConference;
    }

    public void setIdConference(String idConference) {
        this.idConference = idConference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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
        Announcement that = (Announcement) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, picture, url);
    }
}
