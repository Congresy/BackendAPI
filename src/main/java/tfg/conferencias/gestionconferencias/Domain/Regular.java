package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "regular")
public class Regular extends Event {

    public Regular(String name, Integer duration, String place, Date start, Date end, Integer allowedParticipants,
                   Conference conference, Video video) {
        super(name, duration, place, start, end, allowedParticipants, conference, video);
    }
}
