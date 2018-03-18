package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Document
public class Actor {

    @Id
    @JsonIgnore
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Pattern(regexp = "^(\\d{9}|\\d{14})$")
    private String phone;
    @URL
    @NotBlank
    private String photo;
    @NotBlank
    private String nick;
    @NotNull
    private boolean banned;
    @NotNull
    private boolean private_;

    public Actor(){

    }

    public Actor(String name, String surname, String email, String phone, String photo, String nick, String place) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.nick = nick;
        this.banned = false;
        this.private_ = false;
        this.place = place;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isPrivate_() {
        return private_;
    }

    public void setPrivate_(boolean private_) {
        this.private_ = private_;
    }

    // --------------------------------------------------------------------------------------------------------------

    private List<String> conferences;
    //@NotNull
    private String place;

    public List<String> getConferences() {
        return conferences;
    }

    public void setConferences(List<String> conferences) {
        this.conferences = conferences;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

}
