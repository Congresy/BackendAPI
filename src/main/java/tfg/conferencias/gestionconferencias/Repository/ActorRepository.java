package tfg.conferencias.gestionconferencias.Repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tfg.conferencias.gestionconferencias.Domain.Actor;

import java.util.List;

@Repository
public interface ActorRepository extends MongoRepository<Actor, String> {

    public Actor findByName(String name);
    public List<Actor> findBySurname(String surname);//
    public Actor findById(String id);

}
