package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.SocialNetwork;
import com.conferencias.tfg.domain.UserAccount;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.UserAccountRepository;
import com.conferencias.tfg.service.ActorService;
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
import java.util.Set;

@RestController
@RequestMapping("actors")
@Api(value = "Actors", description = "Operations related with actors")
public class Actors {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @GetMapping("")
    @ApiOperation("View a list of all available actors")
    public List<Actor> showAll() {

        return actorRepository.findAll();
    }

    /** Crea un actor según los valores que se envien en el método POST */
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody ActorWrapper actorWrapper, UriComponentsBuilder ucBuilder) {
        if (this.actorExist(actorWrapper.getActor())) {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }

        UserAccount userAccountAux = new UserAccount();
        userAccountAux.setUsername(actorWrapper.getUserAccount().getUsername());
        userAccountAux.setPassword(actorService.encryptPassword(actorWrapper.getUserAccount().getPassword()));
        userAccountRepository.save(userAccountAux);
        Actor aux = new Actor();
        actorWrapper.getActor().setId(aux.getId());
        actorWrapper.getActor().setBanned(false);
        actorWrapper.getActor().setPrivate_(false);
        actorWrapper.getActor().setUserAccount(userAccountAux.getId());
        actorRepository.save(actorWrapper.getActor());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/actor/{id}").buildAndExpand(actorWrapper.getActor().getId()).toUri());
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
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);
        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        actorRepository.delete(id);
        return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?> getAllByRole(@PathVariable("role") String role) {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.getRole().equals(role))
                actors.add(a);
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/place/{place}")
    public ResponseEntity<?> getAllByPlace(@PathVariable("place") String place) {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.getPlace().toLowerCase().contains(place.toLowerCase()))
                actors.add(a);
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/banned")
    public ResponseEntity<?> getBanned() {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.isBanned()) {
                actors.add(a);
            }
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/private")
    public ResponseEntity<?> getPrivate() {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.isPrivate_()) {
                actors.add(a);
            }
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> getAllByKeyword(@PathVariable("keyword") String keyword) {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if ((a.getName() + a.getSurname() + a.getNick()).toLowerCase().contains(keyword.toLowerCase())) {
                actors.add(a);
            }
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @GetMapping("/socialNetwork/{id}")
    public ResponseEntity<?> getSocialByUser(@PathVariable("id") String id) {
        Actor aux = actorRepository.findOne(id);

        return new ResponseEntity<>(aux.getSocialNetworks(), HttpStatus.OK);
    }

    /** Añade una red social a un actor */
    @PostMapping("/socialNetwork/create")
    public ResponseEntity<?> createSocialNetwork(@RequestBody SocialNetwork socialNetwork, @RequestBody Actor actor, UriComponentsBuilder ucBuilder) {
        Set<SocialNetwork> aux = actor.getSocialNetworks();
        aux.add(socialNetwork);
        actor.setSocialNetworks(aux);
        actorRepository.save(actor);

        return new ResponseEntity<>(actor.getSocialNetworks(), HttpStatus.CREATED);
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
