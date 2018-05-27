package com.conferencias.tfg.domain;

import com.conferencias.tfg.utilities.Views.Default;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class Event {

    @Id
    private String id;
    @NotBlank
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @JsonView(Default.class)
    private String start;
    @Pattern(regexp = "^\\d{2}\\/\\d{2}\\/\\d{4}\\s*(?:\\d{2}:\\d{2}(?::\\d{2})?)?$")
    @JsonView(Default.class)
    private String end;
    @NotBlank
    @JsonView(Default.class)
    private String name;
    @JsonView(Default.class)
    private String type; // Para social events
    @JsonView(Default.class)
    @NotBlank
    private String requirements;
    @JsonView(Default.class)
    @NotBlank
    @Pattern(regexp = "^(SocialEvent|Ordinary|Invitation|Workshop)$")
    private String role;


    public Event(){

    }

    public Event(String id, String start, String end, String name, String type, String requeriments, String role, String place) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.name = name;
        this.type = type;
        this.requirements = requeriments;
        this.role = role;
        this.speakers = new ArrayList<>();
        this.place = place;
    }

    public Event(String start, String end, String name, String type, String requeriments, String role, String place) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.type = type;
        this.requirements = requeriments;
        this.role = role;
        this.speakers = new ArrayList<>();
        this.place = place;
    }

    public Event(String start, String end, String name, String requeriments, String role, String place) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.requirements = requeriments;
        this.role = role;
        this.speakers = new ArrayList<>();
        this.place = place;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @NotEmpty
    @NotNull
    @JsonView(Default.class)
    private List<String> speakers;
    @NotNull
    @JsonView(Default.class)
    private String place;
    @JsonView(Default.class)
    private String conference;

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public List<String> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<String> speakers) {
        this.speakers = speakers;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(start, event.start) &&
                Objects.equals(end, event.end) &&
                Objects.equals(name, event.name) &&
                Objects.equals(place, event.place);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, start, end, name, place);
    }
}
