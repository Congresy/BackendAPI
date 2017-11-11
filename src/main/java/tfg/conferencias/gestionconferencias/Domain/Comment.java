package tfg.conferencias.gestionconferencias.Domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comment")
public class Comment extends DomainEntity{
}
