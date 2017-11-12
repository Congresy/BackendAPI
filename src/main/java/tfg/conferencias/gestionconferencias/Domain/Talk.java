package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "talk")
public abstract class Talk extends Event {

    @NotNull
    private List<Speaker> speakers;

    public Talk(String name, Integer duration, String place, Date start, Date end, Integer allowedParticipants,
                Conference conference, Video video, List<Speaker> speakers) {
        super(name, duration, place, start, end, allowedParticipants, conference, video);
        this.speakers = speakers;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }
}
