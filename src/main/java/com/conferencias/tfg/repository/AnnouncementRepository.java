package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Announcement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnnouncementRepository extends MongoRepository<Announcement, String> {
}
