package com.conferencias.tfg.domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;

@Document
public class Post {

    @Id
    private String id;
    @NotBlank
    private String author;
    @NotBlank
    private String title;
    @NotBlank
    private String body;
    @NotBlank
    private String category;
    @NotBlank
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    private String posted;
    @NotNull
    private Integer votes;
    @NotNull
    @Min(0)
    private Integer views;
    @NotNull
    private Boolean draft;

    public Post(){

    }

    public Post(String id, String author, String title, String body, String category, String posted, List<String> comments) {
        this.id = id;
        this.author = author;
        this.body = body;
        this.category = category;
        this.comments = comments;
        this.title = title;
        this.posted = posted;
        this.votes = 0;
        this.views = 0;
        this.draft = true;
    }


    public Post(String author, String title, String body, String category, String posted, List<String> comments) {
        this.author = author;
        this.body = body;
        this.category = category;
        this.comments = comments;
        this.title = title;
        this.posted = posted;
        this.votes = 0;
        this.views = 0;
        this.draft = true;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    // -------------------------------------------------------------------

    private List<String> comments;

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(posted, post.posted) &&
                Objects.equals(votes, post.votes) &&
                Objects.equals(views, post.views);
    }

    @Override
    public int hashCode() {

        return Objects.hash(posted, votes, views);
    }
}
