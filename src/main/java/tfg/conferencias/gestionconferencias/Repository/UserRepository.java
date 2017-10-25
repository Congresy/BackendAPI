package tfg.conferencias.gestionconferencias.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import tfg.conferencias.gestionconferencias.Domain.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    public User findByFirstName(String firstName);
    public List<User> findByLastName(String lastName);
    public User findById(String id);

}
