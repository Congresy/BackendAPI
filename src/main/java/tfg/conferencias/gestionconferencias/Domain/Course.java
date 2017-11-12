package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "course")
public class Course extends Event {

    @NotNull
    private String requirements;
    @NotNull
    private String previousKnowledge;

    public Course(String name, Integer duration, String place, Date start, Date end, Integer allowedParticipants,
                  Conference conference, Video video, String requirements, String previousKnowledge) {
        super(name, duration, place, start, end, allowedParticipants, conference, video);
        this.requirements = requirements;
        this.previousKnowledge = previousKnowledge;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getPreviousKnowledge() {
        return previousKnowledge;
    }

    public void setPreviousKnowledge(String previousKnowledge) {
        this.previousKnowledge = previousKnowledge;
    }
}
