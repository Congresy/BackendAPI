package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {

}
