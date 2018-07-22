package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Folder;
import com.conferencias.tfg.domain.Post;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.FolderRepository;
import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("folders")
@Api(value="congresy", description="Operations pertaining to folders in Congresy")
public class Folders {

    private FolderRepository folderRepository;
    private ActorRepository actorRepository;

    @Autowired
    public Folders(FolderRepository folderRepository, ActorRepository actorRepository) {
        this.folderRepository = folderRepository;
        this.actorRepository = actorRepository;
    }

    @ApiOperation(value = "List all folders of an certain actor", response = Iterable.class)
    @GetMapping("/{idActor}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfActor(@PathVariable("idActor") String id) {
        Actor actor = actorRepository.findOne(id);
        List<String> foldersAux;
        List<Folder> folders = new ArrayList<>();

        try {
            foldersAux = actor.getFolders();

            for(String s : foldersAux){
                folders.add(folderRepository.findOne(s));
            }

        } catch (NullPointerException e){
            folders = new ArrayList<>();
        }

        return new ResponseEntity<>(folders, HttpStatus.OK);
    }

    @ApiOperation(value = "Create all default folders for an actor")
    @PostMapping(value = "/{idActor}", produces = "application/json")
    public ResponseEntity<?> createDefaults(@PathVariable("idActor") String id) {
        Actor actor = actorRepository.findOne(id);

        Folder folder1 = new Folder("Inbox");
        Folder folder2 = new Folder("Outbox");
        Folder folder3 = new Folder("Trash");

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
