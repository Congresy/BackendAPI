package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Conference;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConferenceRepository extends MongoRepository<Conference, String> {

}
