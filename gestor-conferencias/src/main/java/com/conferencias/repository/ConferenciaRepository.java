package com.conferencias.repository;

import com.conferencias.model.Conferencia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenciaRepository extends MongoRepository<Conferencia,String>{

}
