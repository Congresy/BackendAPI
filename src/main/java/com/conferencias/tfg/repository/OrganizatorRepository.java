package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.domain.Organizator;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrganizatorRepository extends MongoRepository<Organizator, String> {

}
