package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.domain.Speaker;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpeakerRepository extends MongoRepository<Speaker, Long> {

}
