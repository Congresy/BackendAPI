package com.conferencias.tfg.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.conferencias.tfg.domain.Conference;

public interface ConferenceRepository extends MongoRepository<Conference, Long> {

}
