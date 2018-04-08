package com.conferencias.tfg.repository;

import com.conferencias.tfg.domain.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FolderRepository extends MongoRepository<Folder, String> {

}
