package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "organizator")
public class Organizer extends Actor {

    public Organizer(String name, String surname, String email, String phone, Address address) {
        super(name, surname, email, phone, address);
    }
}
