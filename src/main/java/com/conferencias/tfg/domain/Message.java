package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

@Document
public class Message {

    @Id
    @JsonIgnore
    private String id;
    @NotBlank
    @JsonView(Views.Default.class)
    private String receiverId;
    @NotBlank
    @JsonView(Views.Default.class)
    private String senderId;
    @NotBlank
    @JsonView(Views.Default.class)
    private String subject;
    @NotBlank
    @JsonView(Views.Default.class)
    private String body;
    @NotBlank
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @JsonView(Views.Default.class)
    private String sentMoment;

    public Message(){}

    public Message(String subject, String body, String sentMoment) {
        this.subject = subject;
        this.body = body;
        this.sentMoment = sentMoment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSentMoment() {
        return sentMoment;
    }

    public void setSentMoment(String sentMoment) {
        this.sentMoment = sentMoment;
    }

}
