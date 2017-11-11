package tfg.conferencias.gestionconferencias.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tfg.conferencias.gestionconferencias.Domain.Actor;
import tfg.conferencias.gestionconferencias.Repository.ActorRepository;

import java.util.Collection;

@Service
public class ActorService{

    @Autowired
    private ActorRepository actorRepository;

    public ActorService(){
    }

    public Collection<Actor> findAll(){
        return actorRepository.findAll();
    }
}
