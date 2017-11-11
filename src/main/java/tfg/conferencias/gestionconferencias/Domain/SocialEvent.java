package tfg.conferencias.gestionconferencias.Domain;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(collection = "socialEvent")
public class SocialEvent extends DomainEntity{

    @NotBlank
    private String type;
    @NotBlank
    private String place;
    @NotNull
    private Date start;
    @NotNull
    private Date end;



}
