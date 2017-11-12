package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "user")
public abstract class User extends Actor {

    @NotNull
    private Calendar calendar;
    private List<User> friendUser;
    private SocialNetwork socialNetwork;
    private List<Post> posts;

    private List<Event> events;

    public User(String name, String surname, String email, String phone, Address address, Calendar calendar,
                List<User> friendUser, SocialNetwork socialNetwork) {
        super(name, surname, email, phone, address);
        this.calendar = calendar;
        this.friendUser = friendUser;
        this.socialNetwork = socialNetwork;
        this.posts = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public List<User> getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(List<User> friendUser) {
        this.friendUser = friendUser;
    }

    public SocialNetwork getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
