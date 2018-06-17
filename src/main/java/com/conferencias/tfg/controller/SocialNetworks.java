package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.SocialNetwork;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.SocialNetworkRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("socialNetworks")
@Api(value = "SocialNetworks", description = "Operations related with social networks")
public class SocialNetworks {

    private final SocialNetworkRepository socialNetworkRepository;
    private final ActorRepository actorRepository;

    @Autowired
    public SocialNetworks(ActorRepository actorRepository, SocialNetworkRepository socialNetworkRepository) {
        this.actorRepository = actorRepository;
        this.socialNetworkRepository = socialNetworkRepository;
    }

    @ApiOperation(value = "Get all social networks for an actor", response = Iterable.class)
    @GetMapping("/{id}")
    public ResponseEntity<?> getSocialNetworksByActor(@PathVariable("id") String id) {
        Actor aux = actorRepository.findOne(id);
        List<SocialNetwork> socialNetworks = new ArrayList<>();

        if (aux.getSocialNetworks().isEmpty() || aux.getSocialNetworks() == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for(String sn : aux.getSocialNetworks()){
                socialNetworks.add(socialNetworkRepository.findOne(sn));
        }

        return new ResponseEntity<>(socialNetworks, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a social network")
    @PostMapping(value = "/{idActor}", produces = "application/json")
    public ResponseEntity<?> createSocialNetwork(@RequestBody SocialNetwork socialNetwork, @PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<String> aux;

        if (actor.getSocialNetworks() == null) {
            aux = new ArrayList<>();
            socialNetwork.setActor(actor.getId());
            socialNetworkRepository.save(socialNetwork);
            aux.add(socialNetwork.getId());
            actor.setSocialNetworks(aux);
            actorRepository.save(actor);

        } else {
            aux = actor.getSocialNetworks();
            socialNetwork.setActor(actor.getId());
            socialNetworkRepository.save(socialNetwork);
            aux.add(socialNetwork.getId());
            actor.setSocialNetworks(aux);
            actorRepository.save(actor);
        }

        return new ResponseEntity<>(socialNetwork, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit a social network")
    @PutMapping(value = "/{idSocialNetwork}", produces = "application/json")
    public ResponseEntity<?> editSocialNetwork(@RequestBody SocialNetwork newSocialNetwork, @PathVariable("idSocialNetwork") String idSocialNetwork) {
        SocialNetwork oldSocialNetwork = socialNetworkRepository.findOne(idSocialNetwork);

        oldSocialNetwork.setName(newSocialNetwork.getName());
        oldSocialNetwork.setUrl(newSocialNetwork.getUrl());

        socialNetworkRepository.save(oldSocialNetwork);

        return new ResponseEntity<>(oldSocialNetwork, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete a social network")
    @DeleteMapping(value = "/{idSocialNetwork}", produces = "application/json")
    public ResponseEntity<?> deleteSocialNetwork(@PathVariable("idSocialNetwork") String idSocialNetwork) {
        SocialNetwork socialNetwork = socialNetworkRepository.findOne(idSocialNetwork);
        String idActor = socialNetwork.getActor();
        Actor actor = null;

        for (Actor a : actorRepository.findAll()){
            if(a.getId().equals(idActor)){
                actor = a;
            }
        }

        List<String> socialNetworks = actor.getSocialNetworks();

        for (String s : socialNetworks){
            if(s.equals(idSocialNetwork)){
                List<String> socialNetworksAux = actor.getSocialNetworks();
                socialNetworksAux.remove(s);
                actorRepository.save(actor);
            }
        }

        socialNetworkRepository.delete(socialNetwork);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
