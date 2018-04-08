package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
