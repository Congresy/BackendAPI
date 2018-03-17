package com.conferencias.tfg.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "event")
public class Event {

    @Id
    @JsonIgnore
    private long id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime start;                                //TODO eliminar 'end' del evento en el modelo conceptual
    @NotBlank
    private String name;
    @Min(0)
    private Integer duration;
    @Min(1)
    private Integer allowedParticipants;

    public Event(){

    }

    public Event(LocalDateTime start, String name, Integer duration, Integer allowedParticipants, Long place) {
        this.start = start;
        this.name = name;
        this.duration = duration;
        this.allowedParticipants = allowedParticipants;
        this.place = place;
        this.participants = new ArrayList<>();
        this.speakers = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    private List<Long> participants;
    @NotEmpty
    private List<Long> speakers;
    @NotNull
    private Long conference;
    //@NotNull
    private Long place;                                    //TODO cambiar en el modelo conceptual

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }

    public List<Long> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Long> speakers) {
        this.speakers = speakers;
    }

    public Long getConference() {
        return conference;
    }

    public void setConference(Long conference) {
        this.conference = conference;
    }

    public Long getPlace() {
        return place;
    }

    public void setPlace(Long place) {
        this.place = place;
    }
}
