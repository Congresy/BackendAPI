package tfg.conferencias.gestionconferencias;

import java.sql.Date;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import tfg.conferencias.gestionconferencias.Domain.Actor;
import tfg.conferencias.gestionconferencias.Domain.Address;
import tfg.conferencias.gestionconferencias.Domain.Conference;
import tfg.conferencias.gestionconferencias.Service.ConferenceService;

public class Populate{


    public static void main(String [] args)
    {
        MongoClientURI uri = new MongoClientURI("mongodb://tfgconferenciasus:OAwlar8I9wkSvQFH@clusterconferencias-shard-00-00-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-01-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-02-gkx6b.mongodb.net:27017/conferencias?ssl=true&replicaSet=ClusterConferencias-shard-0&authSource=admin");
        MongoOperations mongoOps = new MongoTemplate(new MongoClient(uri), "conferencias");
//        Actor actor1 = new Actor("Nuevo version 1", "Actor", "nuevo@actor.com", "123456789", new Address("Sevilla", "España", "41950", "C/Clavel 23"));
//        actor1.setId("123123");
//        Conference conference1 = new Conference("GIT", 2.,"Repositories", 10.,10.,new Date(2017,12,12), new Date(2017,14,12));
//        mongoOps.save(conference1);
    }
}