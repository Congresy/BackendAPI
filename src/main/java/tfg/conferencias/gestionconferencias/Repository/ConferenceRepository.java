package tfg.conferencias.gestionconferencias.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import tfg.conferencias.gestionconferencias.Domain.Conference;

public interface ConferenceRepository extends MongoRepository<Conference, String> {

}
