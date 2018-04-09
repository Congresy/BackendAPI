package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Document
public class Comment {

    @Id
    @JsonIgnore
    private String id;
    @NotBlank
    @JsonView(Views.Default.class)
    private String title;
    @NotBlank
    @JsonView(Views.Default.class)
    private String text;
    @NotNull
    @JsonView(Views.Default.class)
    private Integer thumbsUp;
    @NotNull
    @JsonView(Views.Default.class)
    private Integer thumbsDown;
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @NotBlank
    private String sentMoment;

    public Comment(){}

    public Comment(String id, String title, String text, String sentMoment) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.thumbsUp = 0;
        this.thumbsDown = 0;
        this.sentMoment = sentMoment;
    }

    public Comment(String title, String text, String sentMoment) {
        this.title = title;
        this.text = text;
        this.thumbsUp = 0;
        this.thumbsDown = 0;
        this.sentMoment = sentMoment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getThumbsUp() {
        return thumbsUp;
    }

    public void setThumbsUp(Integer thumbsUp) {
        this.thumbsUp = thumbsUp;
    }

    public Integer getThumbsDown() {
        return thumbsDown;
    }

    public void setThumbsDown(Integer thumbsDown) {
        this.thumbsDown = thumbsDown;
    }

    public String getSentMoment() {
        return sentMoment;
    }

    public void setSentMoment(String sentMoment) {
        this.sentMoment = sentMoment;
    }

    // -----------------------------------------------------------------------------

    @JsonView(Views.Default.class)
    private List<String> responses;

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }
}
