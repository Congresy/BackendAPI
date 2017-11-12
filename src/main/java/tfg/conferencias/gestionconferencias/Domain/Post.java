package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Document(collection = "post")
public class Post extends Commentable {

    @NotBlank
    private String title;
    @NotBlank
    private String category;
    @NotBlank
    private String text;

    @NotNull
    private User user;

    public Post(String title, String category, String text, User user) {
        super();
        this.title = title;
        this.category = category;
        this.text = text;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
