package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Regular;
import com.conferencias.tfg.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegularRepository extends MongoRepository<Regular, String> {

}
