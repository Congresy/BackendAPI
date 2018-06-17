package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.SocialNetwork;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SocialNetworkRepository extends MongoRepository<SocialNetwork, String> {


}
