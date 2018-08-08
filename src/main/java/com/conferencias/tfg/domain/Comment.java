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
import java.util.Objects;

@Document
public class Comment {

    @JsonView(Views.Default.class)
    @Id
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
    @JsonView(Views.Default.class)
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @NotBlank
    private String sentMoment;
    @JsonView(Views.Default.class)
    private String author;

    public Comment(){}

    public Comment(String id, String title, String text, String sentMoment, String author) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.thumbsUp = 0;
        this.thumbsDown = 0;
        this.sentMoment = sentMoment;
        this.author = author;
    }

    public Comment(String title, String text, String sentMoment, String author) {
        this.title = title;
        this.text = text;
        this.thumbsUp = 0;
        this.thumbsDown = 0;
        this.sentMoment = sentMoment;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    @JsonView(Views.Default.class)
    private String commentable;

    public String getCommentable() {
        return commentable;
    }

    public void setCommentable(String commentable) {
        this.commentable = commentable;
    }

    public List<String> getResponses() {
        return responses;
    }

    public void setResponses(List<String> responses) {
        this.responses = responses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(title, comment.title) &&
                Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, text);
    }
}
