package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Actor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ActorRepository extends MongoRepository<Actor, String> {

}
