package com.conferencias.tfg.utilities;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

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
		mongo.dropCollection("event");

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ PLACES --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Place place1 = new Place("Ciudad1", "Pais1", "Direccion1", "Código postal1");
        Place place2 = new Place("Ciudad2", "Pais2", "Direccion2", "Código postal2");
        Place place3 = new Place("Ciudad3", "Pais3", "Direccion3", "Código postal3");
        Place place4 = new Place("Ciudad4", "Pais4", "Direccion4", "Código postal4");

		mongo.save(place1);
        mongo.save(place2);
        mongo.save(place3);
        mongo.save(place4);


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

        Event socialEvent1 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial1", 100, "TipoActividadSocial1", "RequisitosEventoSocial1", "socialEvent", place1.getId());
        Event socialEvent2 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial2", 8, "TipoActividadSocial2", "RequisitosEventoSocial2", "socialEvent", place2.getId());
        Event socialEvent3 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial3", 60, "TipoActividadSocial3", "RequisitosEventoSocial3", "socialEvent", place3.getId());
        Event socialEvent4 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial4", 11, "TipoActividadSocial4", "RequisitosEventoSocial4", "socialEvent", place4.getId());

        mongo.save(socialEvent1);
        mongo.save(socialEvent2);
        mongo.save(socialEvent3);
        mongo.save(socialEvent4);

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- REGULARS -------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event ordinary1 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario1", 56, "RequisitosEventoOrdinario1", "ordinary", place1.getId());
        Event ordinary2 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario2", 31, "RequisitosEventoOrdinario2", "ordinary", place2.getId());
        Event ordinary3 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario3", 72, "RequisitosEventoOrdinario3", "ordinary", place3.getId());
        Event ordinary4 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario4", 17, "RequisitosEventoOrdinario4", "ordinary", place4.getId());

        mongo.save(ordinary1);
        mongo.save(ordinary2);
        mongo.save(ordinary3);
        mongo.save(ordinary4);


        // ----------------------------------------------------------------------------------------------
        // ------------------------------------------ GUESTS --------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event invitation1 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados1", 17, "RequisitosEventoInvitados1", "invitation", place1.getId());
        Event invitation2 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados2", 52, "RequisitosEventoInvitados2", "invitation", place2.getId());
        Event invitation3 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados3", 55, "RequisitosEventoInvitados3", "invitation", place3.getId());
        Event invitation4 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados4", 12, "RequisitosEventoInvitados4", "invitation", place4.getId());

        mongo.save(invitation1);
        mongo.save(invitation2);
        mongo.save(invitation3);
        mongo.save(invitation4);

        // ----------------------------------------------------------------------------------------------
        // ------------------------------------------ COURSES -------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event workshop1 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller1", 16, "RequisitosEventoTaller1", "workshop", place1.getId());
        Event workshop2 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller2", 21, "RequisitosEventoTaller1", "workshop", place2.getId());
        Event workshop3 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller3", 56, "RequisitosEventoTaller1", "workshop", place3.getId());
        Event workshop4 = new Event("10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller4", 71, "RequisitosEventoTaller1", "workshop", place4.getId());

        mongo.save(workshop1);
        mongo.save(workshop2);
        mongo.save(workshop3);
        mongo.save(workshop4);

        // ----------------------------------------------------------------------------------------------
        // --------------------------------------- CONFERENCES ------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Conference conference1 = new Conference("NombreConferencia1", "TemaConferencia1", 0.0, "10/10/2018", "15/10/2018", "InvitadosConferencia1", "DescripciónConferencia1");
        List<String> events1 = new ArrayList<>();
        events1.add(socialEvent1.getId());
        events1.add(workshop1.getId());
        events1.add(ordinary1.getId());
        events1.add(invitation1.getId());
        conference1.setEvents(events1);
        Conference conference2 = new Conference("NombreConferencia2", "TemaConferencia2", 2.0, "10/10/2018", "15/10/2018", "InvitadosConferencia2", "DescripciónConferencia2");
        List<String> events2 = new ArrayList<>();
        events1.add(socialEvent2.getId());
        events1.add(workshop2.getId());
        events1.add(ordinary2.getId());
        events1.add(invitation2.getId());
        conference1.setEvents(events2);
        Conference conference3 = new Conference("NombreConferencia3", "TemaConferencia3", 5.0, "10/10/2018", "15/10/2018", "InvitadosConferencia3", "DescripciónConferencia3");
        List<String> events3 = new ArrayList<>();
        events1.add(socialEvent3.getId());
        events1.add(workshop3.getId());
        events1.add(ordinary3.getId());
        events1.add(invitation3.getId());
        conference1.setEvents(events3);
        Conference conference4 = new Conference("NombreConferencia4", "TemaConferencia4", 8.0, "10/10/2018", "15/10/2018", "InvitadosConferencia4", "DescripciónConferencia4");
        List<String> events4 = new ArrayList<>();
        events1.add(socialEvent4.getId());
        events1.add(workshop4.getId());
        events1.add(ordinary4.getId());
        events1.add(invitation4.getId());
        conference1.setEvents(events4);

        mongo.save(conference1);
        mongo.save(conference2);
        mongo.save(conference3);
        mongo.save(conference4);

	}
}
