package com.conferencias.tfg.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Post {

    // privates ....

    public Post(){

    }


    // constructor y gets y sets ...


    // -------------------------------------------------------------------

    private List<String> comments;

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
