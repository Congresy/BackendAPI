package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Guest;
import com.conferencias.tfg.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GuestRepository extends MongoRepository<Guest, Long> {

}
