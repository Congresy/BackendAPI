package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "event")
public abstract class Event extends DomainEntity {

    @NotBlank
    private String name;
    @NotNull
    private Integer duration;
    @NotNull
    private String place;
    @NotNull
    private Date start;
    @NotNull
    private Date end;
    @NotNull
    private Integer allowedParticipants;

    private List<User> participants;
    @NotNull
    private Conference conference;
    private Video video;

    public Event(String name, Integer duration, String place, Date start, Date end, Integer allowedParticipants,
                 Conference conference, Video video) {
        this.name = name;
        this.duration = duration;
        this.place = place;
        this.start = start;
        this.end = end;
        this.allowedParticipants = allowedParticipants;
        this.participants = new ArrayList<>();
        this.conference = conference;
        this.video = video;
    }

    public String getPlace() {
        return place;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getAllowedParticipants() {
        return allowedParticipants;
    }

    public void setAllowedParticipants(Integer allowedParticipants) {
        this.allowedParticipants = allowedParticipants;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }
}
