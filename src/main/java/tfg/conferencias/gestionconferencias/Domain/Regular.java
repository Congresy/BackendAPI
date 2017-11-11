package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "regular")
public class Regular extends Talk {

    public Regular(String name, Integer duration, Integer freeSeats, Integer takenSeats, Integer totalSeats, Date start, Date end) {
        super(name, duration, freeSeats, takenSeats, totalSeats, start, end);
    }
}
