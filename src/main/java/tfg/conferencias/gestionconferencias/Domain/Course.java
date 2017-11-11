package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "course")
public class Course extends Talk {

    @NotNull
    private String requeriments;
    @NotNull
    private String previousKnowledge;

    public Course(String name, Integer duration, Integer freeSeats, Integer takenSeats, Integer totalSeats, Date start,
                  Date end, String requeriments, String previousKnowledge) {
        super(name, duration, freeSeats, takenSeats, totalSeats, start, end);
        this.requeriments = requeriments;
        this.previousKnowledge = previousKnowledge;
    }

    public String getRequeriments() {
        return requeriments;
    }

    public void setRequeriments(String requeriments) {
        this.requeriments = requeriments;
    }

    public String getPreviousKnowledge() {
        return previousKnowledge;
    }

    public void setPreviousKnowledge(String previousKnowledge) {
        this.previousKnowledge = previousKnowledge;
    }
}
