package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {

}
