package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "attendee")
public class Attendee extends User{

    private List<Speaker> speakersFollowing;

    public Attendee(String name, String surname, String email, String phone, Address address, Calendar calendar,
                    List<User> friendUser, SocialNetwork socialNetwork) {
        super(name, surname, email, phone, address, calendar, friendUser, socialNetwork);
        this.speakersFollowing = new ArrayList<>();
    }

    public List<Speaker> getSpeakersFollowing() {
        return speakersFollowing;
    }

    public void setSpeakersFollowing(List<Speaker> speakersFollowing) {
        this.speakersFollowing = speakersFollowing;
    }


}
