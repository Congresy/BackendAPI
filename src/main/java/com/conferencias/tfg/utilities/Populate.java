package com.conferencias.tfg.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.conferencias.tfg.domain.Actor;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.conferencias.tfg.domain.Address;
import com.conferencias.tfg.domain.Conference;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

public class Populate {

	public static void main(String[] args) {
		MongoURI uri = new MongoURI(
				"mongodb://tfgconferenciasus:OAwlar8I9wkSvQFH@clusterconferencias-shard-00-00-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-01-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-02-gkx6b.mongodb.net:27017/conferencias?ssl=true&replicaSet=ClusterConferencias-shard-0&authSource=admin");
		MongoTemplate mongo = new MongoTemplate(new Mongo(uri), "conferencias");
		mongo.dropCollection("conference");
		mongo.dropCollection("actor");

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ ACTORS --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Actor actor1 = new Actor("José", "Pérez Garrido", "email@us.es", "987678152"
				, "http://conceptodefinicion.de/wp-content/uploads/2014/10/persona.jpg", "jopega");


		// ----------------------------------------------------------------------------------------------
		// --------------------------------------- CONFERENCES ------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Conference conference1 = new Conference("Ciencias de la tierra", "Medioambiente",
				new Date(2017, 12, 12, 12, 00),
				new Date(2017, 12, 19, 12, 00), 20.,
				"Sevilla, España, Avda. de la Raza", "Juan López", actor1.getId());

		conference1.setVideo("https://www.youtube.com");

        List<Long> conferencesActor1 = new ArrayList<>();
        conferencesActor1.add(conference1.getId());
        actor1.setConferences(conferencesActor1);

		mongo.save(conference1);
		mongo.save(actor1);

	}

}
