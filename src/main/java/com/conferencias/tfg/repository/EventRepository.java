package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

}
