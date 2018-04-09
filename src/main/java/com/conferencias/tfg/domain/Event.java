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

@Document
public class Event {

    @Id
    @JsonIgnore
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
    @Min(1)
    @JsonView(Default.class)
    private Integer allowedParticipants;
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

    public Event(String id, String start, String end, String name, Integer allowedParticipants, String type, String requeriments, String role, String place) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.name = name;
        this.allowedParticipants = allowedParticipants;
        this.type = type;
        this.requirements = requeriments;
        this.role = role;
        this.participants = new ArrayList<>();
        this.speakers = new ArrayList<>();
        this.place = place;
    }

    public Event(String start, String end, String name, Integer allowedParticipants, String type, String requeriments, String role, String place) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.allowedParticipants = allowedParticipants;
        this.type = type;
        this.requirements = requeriments;
        this.role = role;
        this.participants = new ArrayList<>();
        this.speakers = new ArrayList<>();
        this.place = place;
    }

    public Event(String id, String start, String end, String name, Integer allowedParticipants, String requeriments, String role, String place) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.name = name;
        this.allowedParticipants = allowedParticipants;
        this.requirements = requeriments;
        this.role = role;
        this.participants = new ArrayList<>();
        this.speakers = new ArrayList<>();
        this.place = place;
    }

    public Event(String start, String end, String name, Integer allowedParticipants, String requeriments, String role, String place) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.allowedParticipants = allowedParticipants;
        this.requirements = requeriments;
        this.role = role;
        this.participants = new ArrayList<>();
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

    public Integer getAllowedParticipants() {
        return allowedParticipants;
    }

    public void setAllowedParticipants(Integer allowedParticipants) {
        this.allowedParticipants = allowedParticipants;
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
    @JsonView(Default.class)
    private List<String> participants;
    @NotEmpty
    @NotNull
    @JsonView(Default.class)
    private List<String> speakers;
    @NotNull
    @JsonView(Default.class)
    private String place;

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
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
}
