package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "video")
public class Video extends DomainEntity {

    @NotNull
    @URL
    private String url;

    @NotNull
    private Event event;

    public Video(String url, Event event) {
        this.url = url;
        this.event = event;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
