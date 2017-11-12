package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Document(collection = "socialNetwork")
public class SocialNetwork extends DomainEntity {

    @NotBlank
    private String name;
    @URL
    private String url;

    @NotNull
    private User user;

    public SocialNetwork(String name, String url, User user) {
        this.name = name;
        this.url = url;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
