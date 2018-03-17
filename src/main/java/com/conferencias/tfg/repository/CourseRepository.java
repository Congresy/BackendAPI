package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Course;
import com.conferencias.tfg.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, Long> {

}
