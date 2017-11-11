package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class Attendee extends User{

    public Attendee(String name, String surname, String email, String phone, Address address) {
        super(name, surname, email, phone, address);
    }
}
