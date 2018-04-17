package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Folder;
import com.conferencias.tfg.domain.Post;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.FolderRepository;
import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("folders")
public class Folders {

    private FolderRepository folderRepository;
    private ActorRepository actorRepository;

    @Autowired
    public Folders(FolderRepository folderRepository, ActorRepository actorRepository) {
        this.folderRepository = folderRepository;
        this.actorRepository = actorRepository;
    }

    @GetMapping("/actor/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfActor(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);
        List<String> foldersAux = actor.getFolders();
        List<Folder> folders = new ArrayList<>();

        for(String s : foldersAux){
                folders.add(folderRepository.findOne(s));
        }

        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @GetMapping("/actor/one/{folder}/{idActor}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getSpecificOfActor(@PathVariable("folder") String folder, @PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<String> foldersAux = actor.getFolders();
        List<Folder> folders = new ArrayList<>();

        for(String s : foldersAux){
            if(folderRepository.findOne(s).getName().equals(folder))
            folders.add(folderRepository.findOne(s));
        }

        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Folder folder = folderRepository.findOne(id);

		if (folder == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(folder, HttpStatus.OK);
	}

    @PostMapping("/create/{id}")
    public ResponseEntity<?> createDefaults(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);

        Folder folder1 = new Folder("Inbox");
        Folder folder2 = new Folder("Outbox");
        Folder folder3 = new Folder("Bin");

        folderRepository.save(folder1);
        folderRepository.save(folder2);
        folderRepository.save(folder3);

        List<Folder> folders = new ArrayList<>();
        folders.add(folder1);
        folders.add(folder2);
        folders.add(folder3);

        List<String> foldersString = new ArrayList<>();
        foldersString.add(folder1.getId());
        foldersString.add(folder2.getId());
        foldersString.add(folder3.getId());

        actor.setFolders(foldersString);
        actorRepository.save(actor);

        return new ResponseEntity<>(folders, HttpStatus.CREATED);
    }

	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//

}
