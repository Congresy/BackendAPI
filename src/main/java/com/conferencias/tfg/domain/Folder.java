package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class Folder {

    @Id
    private String id;
    @NotBlank
    @JsonView(Views.Default.class)
    @Pattern(regexp = "^(Inbox|Outbox|Bin)$")
    private String name;

    public Folder(){}

    public Folder(String id, String name) {
        this.id = id;
        this.name = name;
        this.messages = new ArrayList<>();
    }

    public Folder(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
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

    // -------------------------------------------------------------

    @JsonView(Views.Default.class)
    private List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return Objects.equals(id, folder.id) &&
                Objects.equals(name, folder.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name);
    }
}
