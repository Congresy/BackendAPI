package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "organizer")
public class Organizer extends Actor {

    private List<Conference> conferences;

    public Organizer(String name, String surname, String email, String phone, Address address) {
        super(name, surname, email, phone, address);
        this.conferences = new ArrayList<>();
    }

    public List<Conference> getConferences() {
        return conferences;
    }

    public void setConferences(List<Conference> conferences) {
        this.conferences = conferences;
    }
}
