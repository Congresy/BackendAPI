package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.service.ActorService;
import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.models.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("actor")
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorService actorService;

    @GetMapping("/all")
    public List<Actor> showAll() {

        return actorRepository.findAll();
    }

    /** Crea un actor según los valores que se envien en el método POST */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Actor actor, UriComponentsBuilder ucBuilder) {

        if (this.actorExist(actor)) {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
        Actor aux = new Actor();
        actor.setId(aux.getId());
        actor.setBanned(false);
        actor.setPrivate_(false);
        actorRepository.save(actor);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/actor/{id}").buildAndExpand(actor.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Recibe una id para devolver un actor */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDetailed(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);

        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    /** Modifica un actor con los campos que se indiquen */
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Actor actor) {
        Actor currentActor = actorRepository.findOne(id);

        if (currentActor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        actorService.edit(currentActor,actor);
        return new ResponseEntity<>(currentActor, HttpStatus.OK);
    }

    /** Borra un actor por ID*/
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);
        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        actorRepository.delete(id);
        return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT);
    }

    /** Dado un actor, comprueba si existe uno igual */
    private Boolean actorExist(Actor actor){
        Boolean res = false;

        for (Actor a: actorRepository.findAll()){
            if(actor.equals(a)){
                res = true;
                break;
            }
        }
        return res;
    }
}
