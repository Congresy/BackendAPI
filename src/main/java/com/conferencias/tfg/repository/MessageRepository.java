package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {

}
