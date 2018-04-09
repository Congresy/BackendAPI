package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Announcement {

    @Id
    private String id;
    @URL
    private String picture;
    @URL
    private String url;

    public Announcement() {

    }

    public Announcement(String id, String picture, String url) {
        this.id = id;
        this.picture = picture;
        this.url = url;
    }

    public Announcement(String picture, String url) {
        this.picture = picture;
        this.url = url;
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
}
