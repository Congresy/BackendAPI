package com.conferencias.tfg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Document
public class Actor {

    @Id
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
    @NotBlank
    @Pattern(regexp = "^(Organizator|Speaker|User|Administrator)$")
    private String role;

    private Set<SocialNetwork> socialNetworks;


    @NotNull
    private String userAccount;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public Actor(){

    }

    public Actor(String id, String name, String surname, String email, String phone, String photo, String nick, String place, String role, String userAccount) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.nick = nick;
        this.banned = false;
        this.private_ = false;
        this.place = place;
        this.comments = new ArrayList<>();
        this.role = role;
        this.userAccount = userAccount;
    }

    public Actor(String name, String surname, String email, String phone, String photo, String nick, String place, String role, String userAccount) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
        this.nick = nick;
        this.banned = false;
        this.private_ = false;
        this.place = place;
        this.comments = new ArrayList<>();
        this.role = role;
        this.userAccount = userAccount;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<SocialNetwork> getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(Set<SocialNetwork> socialNetworks) {
        this.socialNetworks = socialNetworks;
    }


    // --------------------------------------------------------------------------------------------------------------

    private List<String> conferences;
    //@NotNull
    private String place;
    @NotNull
    private List<String> comments;

    private List<String> interests;


    private List<String> friends;

    private List<String> following;

    private List<String> folders;

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

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

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id) &&
                Objects.equals(name, actor.name) &&
                Objects.equals(surname, actor.surname) &&
                Objects.equals(email, actor.email) &&
                Objects.equals(phone, actor.phone) &&
                Objects.equals(photo, actor.photo) &&
                Objects.equals(nick, actor.nick) &&
                Objects.equals(role, actor.role) &&
                Objects.equals(userAccount, actor.userAccount) &&
                Objects.equals(place, actor.place);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, surname, email, phone, photo, nick, role, userAccount, place);
    }
}

