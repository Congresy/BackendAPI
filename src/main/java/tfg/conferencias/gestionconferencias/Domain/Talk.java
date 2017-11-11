package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "talk")
public abstract class Talk extends DomainEntity {

    private String name;
    private Integer duration; //In minutes
    private Integer freeSeats;
    private Integer takenSeats;
    private Integer totalSeats;
    private Date start;
    private Date end;

    public Talk(String name, Integer duration, Integer freeSeats, Integer takenSeats, Integer totalSeats,
                Date start, Date end) {
        this.name = name;
        this.duration = duration;
        this.freeSeats = freeSeats;
        this.takenSeats = takenSeats;
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
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

    public Integer getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(Integer freeSeats) {
        this.freeSeats = freeSeats;
    }

    public Integer getTakenSeats() {
        return takenSeats;
    }

    public void setTakenSeats(Integer takenSeats) {
        this.takenSeats = takenSeats;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
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
}
