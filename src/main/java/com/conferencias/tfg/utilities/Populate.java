package com.conferencias.tfg.utilities;

import java.time.LocalDateTime;
import java.time.Month;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Place;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.conferencias.tfg.domain.Conference;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;

public class Populate {

	public static void main(String[] args) {
		MongoURI uri = new MongoURI(
				"mongodb://tfgconferenciasus:OAwlar8I9wkSvQFH@clusterconferencias-shard-00-00-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-01-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-02-gkx6b.mongodb.net:27017/conferencias?ssl=true&replicaSet=ClusterConferencias-shard-0&authSource=admin");
		MongoTemplate mongo = new MongoTemplate(new Mongo(uri), "conferencias");
		mongo.dropCollection("conference");
		mongo.dropCollection("place");
		mongo.dropCollection("actor");

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ PLACES --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Place place1 = new Place("Ciudad", "Pais", "Direccion", "Código postal");

		mongo.save(place1);

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ ACTORS --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Actor actor1 = new Actor("José", "Pérez Garrido", "email@us.es", "987678152"
				, "http://conceptodefinicion.de/wp-content/uploads/2014/10/persona.jpg", "jopega", place1.getId());

		mongo.save(actor1);

		// ----------------------------------------------------------------------------------------------
		// --------------------------------------- CONFERENCES ------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Conference conference1 = new Conference("Nombre", "Tema", 0.0,
				LocalDateTime.of(2019, Month.APRIL, 1, 10, 10,30),
				LocalDateTime.of(2019, Month.APRIL, 20, 10, 10,30),
				"Descripción", "Invitados");

		mongo.save(conference1);


	}

}
