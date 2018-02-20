package com.conferencias.tfg.utilities;

import java.util.Date;

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
		mongo.dropCollection("address");
		Address address = new Address("Sevilla", "España", "Avda. de la Raza");
		Conference conference = new Conference("Ciencias de la tierra", "Medioambiente", new Date(2017, 12, 12, 12, 00),
				new Date(2017, 12, 19, 12, 00), 20., address, "Juan López");
		conference.setVideo("https://www.youtube.com");
		mongo.save(conference);

	}

}
