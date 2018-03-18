package com.conferencias.tfg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.conferencias.tfg.utilities.Views.Default;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
public class Event {

    @Id
    @JsonIgnore
    private String id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonView(Default.class)
    private LocalDateTime start;
    @NotBlank
    @JsonView(Default.class)
    private String name;
    @Min(0)
    @JsonView(Default.class)
    private Integer duration;
    @Min(1)
    @JsonView(Default.class)
    private Integer allowedParticipants;

    public Event(){

    }

    public Event(LocalDateTime start, String name, Integer duration, Integer allowedParticipants, String place) {
        this.start = start;
        this.name = name;
        this.duration = duration;
        this.allowedParticipants = allowedParticipants;
        this.place = place;
        this.participants = new ArrayList<>();
        this.speakers = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getAllowedParticipants() {
        return allowedParticipants;
    }

    public void setAllowedParticipants(Integer allowedParticipants) {
        this.allowedParticipants = allowedParticipants;
    }

    // -----------------------------------------------------------------------------------------------------------------
    @JsonView(Default.class)
    private List<String> participants;
    @NotEmpty
    @JsonView(Default.class)
    private List<String> speakers;
    @NotNull
    @JsonView(Default.class)
    private String conference;
    //@NotNull
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

    public String getConference() {
        return conference;
    }

    public void setConference(String conference) {
        this.conference = conference;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}