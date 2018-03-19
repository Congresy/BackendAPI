package com.conferencias.tfg.service;


import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ActorService{

    @Autowired
    private ActorRepository actorRepository;

    public Actor edit(Actor currentActor,Actor actor){
        currentActor.setName(actor.getName());
        currentActor.setSurname(actor.getSurname());
        currentActor.setEmail(actor.getEmail());
        currentActor.setPhone(actor.getPhone());
        currentActor.setPhoto(actor.getPhoto());
        currentActor.setPlace(actor.getPlace());
        Actor res = actorRepository.save(currentActor);
        return res;
    }
}
