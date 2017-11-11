package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "guest")
public class Guest extends Talk {

    public Guest(String name, Integer duration, Integer freeSeats, Integer takenSeats, Integer totalSeats, Date start, Date end) {
        super(name, duration, freeSeats, takenSeats, totalSeats, start, end);
    }
}
