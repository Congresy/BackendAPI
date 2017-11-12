package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "calendar")
public class Calendar extends DomainEntity {

    @NotBlank
    private String month;

    private List<Event> events;
    @NotNull
    private User user;

    public Calendar(String month, User user) {
        this.month = month;
        this.events = new ArrayList<>();
        this.user = user;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
