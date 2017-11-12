package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "commentable")
public class Commentable extends DomainEntity {

    private List<Comment> comments;

    public Commentable() {
        this.comments = new ArrayList<>();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
