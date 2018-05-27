package com.conferencias.tfg.utilities;

import com.conferencias.tfg.domain.*;
import com.mongodb.Mongo;
import com.mongodb.MongoURI;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Populate {

	public static void main(String[] args) {
		MongoURI uri = new MongoURI(
				"mongodb://tfgconferenciasus:OAwlar8I9wkSvQFH@clusterconferencias-shard-00-00-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-01-gkx6b.mongodb.net:27017,clusterconferencias-shard-00-02-gkx6b.mongodb.net:27017/conferencias?ssl=true&replicaSet=ClusterConferencias-shard-0&authSource=admin");
		MongoTemplate mongo = new MongoTemplate(new Mongo(uri), "conferencias");
		mongo.dropCollection("conference");
		mongo.dropCollection("place");
		mongo.dropCollection("event");
        mongo.dropCollection("comment");
        mongo.dropCollection("actor");
        mongo.dropCollection("userAccount");
        mongo.dropCollection("announcement");
        mongo.dropCollection("post");

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- COMMENTS -------------------------------------------
        // ---------------------------------------------

        Comment comment1 = new Comment("1","TítuloComentario1", "TextoComentario1", "20/12/2017 10:10");
        Comment comment2 = new Comment("2", "TítuloComentario2", "TextoComentario2", "20/12/2017 10:10");
        Comment comment3 = new Comment("3", "TítuloComentario3", "TextoComentario3", "20/12/2017 10:10");
        Comment comment4 = new Comment("4", "TítuloComentario4", "TextoComentario4", "20/12/2017 10:10");
        Comment comment5 = new Comment("5", "TítuloComentario5", "TextoComentario5", "20/12/2017 10:10");
        Comment comment6 = new Comment("6", "TítuloComentario6", "TextoComentario6", "20/12/2017 10:10");
        Comment comment7 = new Comment("7", "TítuloComentario7", "TextoComentario7", "20/12/2017 10:10");
        Comment comment8 = new Comment("8", "TítuloComentario8", "TextoComentario8", "20/12/2017 10:10");
        Comment comment9 = new Comment("9", "TítuloComentario9", "TextoComentario9", "20/12/2017 10:10");

        List<String> respones1 = new ArrayList<>();
        respones1.add(comment6.getId());
        respones1.add(comment7.getId());
        respones1.add(comment8.getId());
        comment1.setResponses(respones1);
        List<String> respones2 = new ArrayList<>();
        respones2.add(comment9.getId());
        comment2.setResponses(respones2);

        mongo.save(comment1);
        mongo.save(comment2);
        mongo.save(comment3);
        mongo.save(comment4);
        mongo.save(comment5);
        mongo.save(comment6);
        mongo.save(comment7);
        mongo.save(comment8);
        mongo.save(comment9);

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ PLACES --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Place place1 = new Place("1", "Ciudad1", "Pais1", "Direccion1", "Código postal1");
        Place place2 = new Place("2", "Ciudad2", "Pais2", "Direccion2", "Código postal2");
        Place place3 = new Place("3", "Ciudad3", "Pais3", "Direccion3", "Código postal3");
        Place place4 = new Place("4", "Ciudad4", "Pais4", "Direccion4", "Código postal4");

		mongo.save(place1);
        mongo.save(place2);
        mongo.save(place3);
        mongo.save(place4);


		// ----------------------------------------------------------------------------------------------
		// ----------------------------------------- SPEAKERS -------------------------------------------
		// ----------------------------------------------------------------------------------------------

//		Speaker speaker1 = new Speaker("Nombre1", "Apellido1", "email1@us.es", "987678152"
//                , "http://imagen1.jpg", "nick1", place1.getId());
//
//        mongo.save(speaker1);

		// ----------------------------------------------------------------------------------------------
		// --------------------------------------- ORGANIZATORS -----------------------------------------
		// ----------------------------------------------------------------------------------------------

//		Organizator organizator1 = new Organizator("Nombre1", "Apellido1", "email1@us.es", "987678152"
//				, "http://imagen1.jpg", "nick1", place1.getId());
//
//		mongo.save(organizator1);

        // ----------------------------------------------------------------------------------------------
        // -------------------------------------- SOCIAL EVENTS -----------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event socialEvent1 = new Event("1","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial1", "TipoActividadSocial1", "RequisitosEventoSocial1", "socialEvent", place1.getId());
        Event socialEvent2 = new Event("2", "10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial2", "TipoActividadSocial2", "RequisitosEventoSocial2", "socialEvent", place2.getId());
        Event socialEvent3 = new Event("3", "10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial3", "TipoActividadSocial3", "RequisitosEventoSocial3", "socialEvent", place3.getId());
        Event socialEvent4 = new Event("4", "10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoSocial4", "TipoActividadSocial4", "RequisitosEventoSocial4", "socialEvent", place4.getId());

        mongo.save(socialEvent1);
        mongo.save(socialEvent2);
        mongo.save(socialEvent3);
        mongo.save(socialEvent4);

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- REGULARS -------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event ordinary1 = new Event("5","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario1", "RequisitosEventoOrdinario1", "ordinary", place1.getId());
        Event ordinary2 = new Event("6","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario2", "RequisitosEventoOrdinario2", "ordinary", place2.getId());
        Event ordinary3 = new Event("7", "10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario3", "RequisitosEventoOrdinario3", "ordinary", place3.getId());
        Event ordinary4 = new Event("8","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoOrdinario4", "RequisitosEventoOrdinario4", "ordinary", place4.getId());

        mongo.save(ordinary1);
        mongo.save(ordinary2);
        mongo.save(ordinary3);
        mongo.save(ordinary4);


        // ----------------------------------------------------------------------------------------------
        // ------------------------------------------ GUESTS --------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event invitation1 = new Event("9","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados1", "RequisitosEventoInvitados1", "invitation", place1.getId());
        Event invitation2 = new Event("10","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados2", "RequisitosEventoInvitados2", "invitation", place2.getId());
        Event invitation3 = new Event("11","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados3", "RequisitosEventoInvitados3", "invitation", place3.getId());
        Event invitation4 = new Event("12","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoInvitados4", "RequisitosEventoInvitados4", "invitation", place4.getId());

        mongo.save(invitation1);
        mongo.save(invitation2);
        mongo.save(invitation3);
        mongo.save(invitation4);

        // ----------------------------------------------------------------------------------------------
        // ------------------------------------------ COURSES -------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Event workshop1 = new Event("13","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller1",  "RequisitosEventoTaller1", "workshop", place1.getId());
        Event workshop2 = new Event("14","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller2",  "RequisitosEventoTaller1", "workshop", place2.getId());
        Event workshop3 = new Event("15","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller3", "RequisitosEventoTaller1", "workshop", place3.getId());
        Event workshop4 = new Event("16","10/10/2018 11:20", "10/10/2018 14:20", "NombreEventoTaller4", "RequisitosEventoTaller1", "workshop", place4.getId());

        mongo.save(workshop1);
        mongo.save(workshop2);
        mongo.save(workshop3);
        mongo.save(workshop4);


        // ----------------------------------------------------------------------------------------------
        // -------------------------------------- ANNOUNCEMENTS -----------------------------------------
        // ----------------------------------------------------------------------------------------------

        Announcement announcement1 = new Announcement("1", "http://www.picture1.jpg", "http://www.url1.com");
        Announcement announcement2 = new Announcement("2", "http://www.picture2.jpg", "http://www.url2.com");
        Announcement announcement3 = new Announcement("3", "http://www.picture3.jpg", "http://www.url3.com");
        Announcement announcement4 = new Announcement("4", "http://www.picture4.jpg", "http://www.url4.com");

        mongo.save(announcement1);
        mongo.save(announcement2);
        mongo.save(announcement3);
        mongo.save(announcement4);

        // ----------------------------------------------------------------------------------------------
        // -------------------------------------- USER ACCOUNTS------------------------------------------
        // ----------------------------------------------------------------------------------------------


        UserAccount userAccount1 = new UserAccount("juan", "juan");
        UserAccount userAccount2 = new UserAccount("luis", "luis");
        UserAccount userAccount3 = new UserAccount("maria", "maria");
        UserAccount userAccount4 = new UserAccount("laura", "laura");

        mongo.save(userAccount1);
        mongo.save(userAccount2);
        mongo.save(userAccount3);
        mongo.save(userAccount4);

        // ----------------------------------------------------------------------------------------------
        // ---------------------------------------- MESSAGES --------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Message message1 = new Message("1", "SubjectMessage1", "BodyMessage1", "10/20/2017");
        Message message2 = new Message("2", "SubjectMessage2", "BodyMessage2", "10/20/2017");
        Message message3 = new Message("3", "SubjectMessage3", "BodyMessage3", "10/20/2017");
        Message message4 = new Message("4", "SubjectMessage4", "BodyMessage4", "10/20/2017");

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- FOLDERS --------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Folder folder1 = new Folder("1", "Inbox");
        Folder folder2 = new Folder("2", "Outbox");
        Folder folder3 = new Folder("3", "Bin");
        Folder folder4 = new Folder("4", "Inbox");
        Folder folder5 = new Folder("5", "Outbox");
        Folder folder6 = new Folder("6", "Bin");
        Folder folder7 = new Folder("7", "Inbox");
        Folder folder8 = new Folder("8", "Outbox");
        Folder folder9 = new Folder("9", "Bin");
        Folder folder10 = new Folder("10", "Inbox");
        Folder folder11 = new Folder("11", "Outbox");
        Folder folder12 = new Folder("12", "Bin");

        List<String> messages1 = new ArrayList<>();
        messages1.add(message1.getId());
        messages1.add(message2.getId());
        folder1.setMessages(messages1);

        List<String> messages2 = new ArrayList<>();
        messages2.add(message3.getId());
        messages2.add(message4.getId());
        folder2.setMessages(messages2);

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- ACTORS ---------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Actor actor1 = new Actor("5", "Juan", "Pérez", "juan@email.com", "111111111", "http://www.juanphoto.jpg", "juan", "Sevilla", "Organizator", userAccount1.getId());
        Actor actor2 = new Actor("6", "Luis", "López", "luis@email.com", "222222222", "http://www.luisphoto.jpg", "luis", "Madrid", "User", userAccount2.getId());
        Actor actor3 = new Actor("7", "María", "Pizarro", "maria@email.com", "333333333", "http://www.mariaphoto.jpg", "maria", "Barcelona", "User", userAccount3.getId());
        Actor actor4 = new Actor("8", "Laura", "Prieto", "laura@email.com", "444444444", "http://www.lauraphoto.jpg", "laura", "Vigo", "Speaker", userAccount4.getId());
        Set<SocialNetwork> aux = new HashSet<>();
        aux.add(new SocialNetwork("Instagram", "https://instagram.com/juan"));
        aux.add(new SocialNetwork("facebook", "https://facebook.com/juan"));
        actor1.setSocialNetworks(aux);

        List<String> folders1 = new ArrayList<>();
        folders1.add(folder1.getId());
        folders1.add(folder2.getId());
        folders1.add(folder3.getId());
        actor1.setFolders(folders1);

        List<String> folders2 = new ArrayList<>();
        folders2.add(folder4.getId());
        folders2.add(folder5.getId());
        folders2.add(folder6.getId());
        actor2.setFolders(folders2);

        List<String> folders3 = new ArrayList<>();
        folders3.add(folder7.getId());
        folders3.add(folder8.getId());
        folders3.add(folder9.getId());
        actor3.setFolders(folders3);

        List<String> folders4 = new ArrayList<>();
        folders4.add(folder10.getId());
        folders4.add(folder11.getId());
        folders4.add(folder12.getId());
        actor4.setFolders(folders4);

        mongo.save(actor1);
        mongo.save(actor2);
        mongo.save(actor3);
        mongo.save(actor4);

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- POSTS ----------------------------------------------
        // ----------------------------------------------------------------------------------------------

        Post post1 = new Post("9",actor1.getId(), "Title1", "Body1", "Category1", "10/10/2018 11:20", new ArrayList<String>());
        Post post2 = new Post("10",actor1.getId(), "Title2", "Body2", "Category2", "10/10/2018 11:20", new ArrayList<String>());
        Post post3 = new Post("11",actor3.getId(), "Title3", "Body3", "Category3", "10/10/2018 11:20", new ArrayList<String>());
        Post post4 = new Post("12", actor2.getId(), "Title4", "Body4", "Category4", "10/10/2018 11:20", new ArrayList<String>());

        mongo.save(post1);
        mongo.save(post2);
        mongo.save(post3);
        mongo.save(post4);

            // ----------------------------------------------------------------------------------------------
            // --------------------------------------- CONFERENCES ------------------------------------------
            // ----------------------------------------------------------------------------------------------

            Conference conference1 = new Conference("1","NombreConferencia1", "TemaConferencia1", 0.0, 20, "10/10/2018 00:00", "15/10/2018 00:00", "InvitadosConferencia1", "DescripciónConferencia1", actor1.getId());
            List<String> events1 = new ArrayList<>();
            events1.add(socialEvent1.getId());
            events1.add(workshop1.getId());
            events1.add(ordinary1.getId());
            events1.add(invitation1.getId());
            conference1.setEvents(events1);
            List<String> comments1 = new ArrayList<>();
            comments1.add(comment1.getId());
            comments1.add(comment2.getId());
            comments1.add(comment3.getId());
            conference1.setComments(comments1);

            Conference conference2 = new Conference("2","NombreConferencia2", "TemaConferencia2", 2.0, 20, "10/10/2018 00:00", "15/10/2018 00:00", "InvitadosConferencia2", "DescripciónConferencia2", actor1.getId());
            List<String> events2 = new ArrayList<>();
            events2.add(socialEvent2.getId());
            events2.add(workshop2.getId());
            events2.add(ordinary2.getId());
            events2.add(invitation2.getId());
            conference2.setEvents(events2);
            List<String> comments2 = new ArrayList<>();
            comments2.add(comment4.getId());
            comments2.add(comment5.getId());
            conference2.setComments(comments2);

            Conference conference3 = new Conference("3","NombreConferencia3", "TemaConferencia3", 5.0, 20, "10/10/2018 00:00", "15/10/2018 00:00", "InvitadosConferencia3", "DescripciónConferencia3", actor1.getId());
            List<String> events3 = new ArrayList<>();
            events3.add(socialEvent3.getId());
            events3.add(workshop3.getId());
            events3.add(ordinary3.getId());
            events3.add(invitation3.getId());
            conference3.setEvents(events3);
            List<String> comments3 = new ArrayList<>();
            comments3.add(comment6.getId());
            conference3.setComments(comments3);

            Conference conference4 = new Conference("4", "NombreConferencia4", "TemaConferencia4", 8.0, 15, "10/10/2018 00:00", "15/10/2018 00:00", "InvitadosConferencia4", "DescripciónConferencia4", actor1.getId());
            List<String> events4 = new ArrayList<>();
            events4.add(socialEvent4.getId());
            events4.add(workshop4.getId());
            events4.add(ordinary4.getId());
            events4.add(invitation4.getId());
            conference4.setEvents(events4);

            mongo.save(conference1);
            mongo.save(conference2);
            mongo.save(conference3);
            mongo.save(conference4);


	}
}
