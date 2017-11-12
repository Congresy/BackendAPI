package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Document(collection = "socialEvent")
public class SocialEvent extends Event{

    @NotBlank
    private String type;

    public SocialEvent(String name, Integer duration, String place, Date start, Date end, Integer allowedParticipants,
                       Conference conference, Video video, String type) {
        super(name, duration, place, start, end, allowedParticipants, conference, video);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
