package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "speaker")
public class Speaker extends User {

    public Speaker(String name, String surname, String email, String phone, Address address) {
        super(name, surname, email, phone, address);
    }
}
