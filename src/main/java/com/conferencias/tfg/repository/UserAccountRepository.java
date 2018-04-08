package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserAccountRepository extends MongoRepository<UserAccount, String> {

    public UserAccount findByUsername(String username);
}
