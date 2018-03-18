package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.domain.Place;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaceRepository extends MongoRepository<Place, String> {

}
