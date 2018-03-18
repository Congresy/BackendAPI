package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.SocialEvent;
import com.conferencias.tfg.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SocialEventRepository extends MongoRepository<SocialEvent, String> {

}
