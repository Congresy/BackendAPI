package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "speaker")
public class Speaker extends User {

    private List<Talk> talkingTalks;

    public Speaker(String name, String surname, String email, String phone, Address address, Calendar calendar,
                   List<User> friendUser, SocialNetwork socialNetwork) {
        super(name, surname, email, phone, address, calendar, friendUser, socialNetwork);
        this.talkingTalks = new ArrayList<>();
    }

    public List<Talk> getTalkingTalks() {
        return talkingTalks;
    }

    public void setTalkingTalks(List<Talk> talkingTalks) {
        this.talkingTalks = talkingTalks;
    }
}
