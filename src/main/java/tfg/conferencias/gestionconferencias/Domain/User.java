package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document(collection = "user")
public class User extends Actor {

    private List<Talk> talks;
    @NotNull
    private Calendar calendar;
    private List<User> friendUser;
    private SocialNetwork socialNetwork;
    private List<Post> posts;

    public User(String name, String surname, String email, String phone, Address address, List<Talk> talks,
                Calendar calendar, List<User> friendUser, SocialNetwork socialNetwork, List<Post> posts) {
        super(name, surname, email, phone, address);
        this.talks = talks;
        this.calendar = calendar;
        this.friendUser = friendUser;
        this.socialNetwork = socialNetwork;
        this.posts = posts;
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
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
}
