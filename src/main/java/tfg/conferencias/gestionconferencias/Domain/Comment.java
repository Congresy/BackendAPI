package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "comment")
public class Comment extends DomainEntity{

    @NotBlank
    private String title;
    @NotBlank
    private String text;
    @NotNull
    @Range(min = 0, max = 10)
    private Integer stars;
    @NotNull
    private Boolean recommended;
    @NotNull
    private Date sent;

    @NotNull
    private Commentable commentable;
    @NotNull
    private Actor actor;

    public Comment(String title, String text, Integer stars, Boolean recommended, Date sent, Commentable commentable,
                   Actor actor) {
        this.title = title;
        this.text = text;
        this.stars = stars;
        this.recommended = recommended;
        this.sent = sent;

        this.commentable = commentable;
        this.actor = actor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Boolean getRecommended() {
        return recommended;
    }

    public void setRecommended(Boolean recommended) {
        this.recommended = recommended;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Commentable getCommentable() {
        return commentable;
    }

    public void setCommentable(Commentable commentable) {
        this.commentable = commentable;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }
}
