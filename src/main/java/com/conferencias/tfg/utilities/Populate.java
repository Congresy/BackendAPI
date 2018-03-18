package com.conferencias.tfg.utilities;

import java.time.LocalDateTime;
import java.time.Month;

import com.conferencias.tfg.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoURI;

public class Populate {

	public static void main(String[] args) {
		MongoURI uri = new MongoURI(
				"mongodb://tfgconferenciasus:OAwlar8I9wkSvQFH@clusterconferencias-shard-00-00-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-01-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-02-gkx6b.mongodb.net:27017/conferencias?ssl=true&replicaSet=ClusterConferencias-shard-0&authSource=admin");
		MongoTemplate mongo = new MongoTemplate(new Mongo(uri), "conferencias");
		mongo.dropCollection("conference");
		mongo.dropCollection("place");
		mongo.dropCollection("speaker");
		mongo.dropCollection("organizator");
		mongo.dropCollection("guest");
		mongo.dropCollection("course");
		mongo.dropCollection("regular");
		mongo.dropCollection("socialEvent");

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ PLACES --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Place place1 = new Place("Ciudad", "Pais", "Direccion", "Código postal");

		mongo.save(place1);

		// ----------------------------------------------------------------------------------------------
		// ----------------------------------------- SPEAKERS -------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Speaker speaker1 = new Speaker("Nombre1", "Apellido1", "email1@us.es", "987678152"
				, "http://imagen1.jpg", "nick1", place1.getId());

		mongo.save(speaker1);

		// ----------------------------------------------------------------------------------------------
		// --------------------------------------- ORGANIZATORS -----------------------------------------
		// ----------------------------------------------------------------------------------------------

		Organizator organizator1 = new Organizator("Nombre1", "Apellido1", "email1@us.es", "987678152"
				, "http://imagen1.jpg", "nick1", place1.getId());

		mongo.save(organizator1);

		// ----------------------------------------------------------------------------------------------
		// -------------------------------------- SOCIAL EVENTS -----------------------------------------
		// ----------------------------------------------------------------------------------------------

		SocialEvent socialEvent1 = new SocialEvent(
				LocalDateTime.of(2019, Month.APRIL, 1, 10, 10,30),
				"Nombre1", 200, 35, place1.getId(), "Tipo1");

		mongo.save(socialEvent1);

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ GUESTS --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Guest guest1 = new Guest(
				LocalDateTime.of(2019, Month.APRIL, 1, 10, 10,30),
				"Nombre1", 200, 35, place1.getId());

		mongo.save(guest1);

		// ----------------------------------------------------------------------------------------------
		// ----------------------------------------- REGULARS -------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Regular regular1 = new Regular(
				LocalDateTime.of(2019, Month.APRIL, 1, 10, 10,30),
				"Nombre1", 200, 35, place1.getId());

		mongo.save(regular1);

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ COURSES -------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Course course1 = new Course(
				LocalDateTime.of(2019, Month.APRIL, 1, 10, 10,30),
				"Nombre1", 200, 35, place1.getId(), "Requisitos1");

		mongo.save(course1);

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
