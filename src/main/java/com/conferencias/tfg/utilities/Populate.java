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
        mongo.dropCollection("actor");
		mongo.dropCollection("announcement");
		mongo.dropCollection("comment");
		mongo.dropCollection("conference");
		mongo.dropCollection("event");
		mongo.dropCollection("folder");
		mongo.dropCollection("message");
		mongo.dropCollection("place");
		mongo.dropCollection("post");
		mongo.dropCollection("socialNetwork");
		mongo.dropCollection("userAccount");



		/*// ----------------------------------------------------------------------------------------------
		// -------------------------------------- USER ACCOUNTS------------------------------------------
		// ----------------------------------------------------------------------------------------------

		UserAccount userAccount1 = new UserAccount("username1", "password1");
		UserAccount userAccount2 = new UserAccount("username2", "password2");
		UserAccount userAccount3 = new UserAccount("username3", "password3");
		UserAccount userAccount4 = new UserAccount("username4", "password4");
		UserAccount userAccount5 = new UserAccount("username5", "password5");
		UserAccount userAccount6 = new UserAccount("username6", "password6");
		UserAccount userAccount7 = new UserAccount("username7", "password7");
		UserAccount userAccount8 = new UserAccount("username8", "password8");
		UserAccount userAccount9 = new UserAccount("username9", "password9");
		UserAccount userAccount10 = new UserAccount("username10", "password10");
		UserAccount userAccount11 = new UserAccount("username11", "password11");
		UserAccount userAccount12 = new UserAccount("username12", "password12");
		UserAccount userAccount13 = new UserAccount("username13", "password13");
		UserAccount userAccount14 = new UserAccount("username14", "password14");
		UserAccount userAccount15 = new UserAccount("username15", "password15");
		UserAccount userAccount16 = new UserAccount("username16", "password16");
		UserAccount userAccount17 = new UserAccount("username17", "password17");
		UserAccount userAccount18 = new UserAccount("username18", "password18");

		mongo.save(userAccount1);
		mongo.save(userAccount2);
		mongo.save(userAccount3);
		mongo.save(userAccount4);
		mongo.save(userAccount5);
		mongo.save(userAccount6);
		mongo.save(userAccount7);
		mongo.save(userAccount8);
		mongo.save(userAccount9);
		mongo.save(userAccount10);
		mongo.save(userAccount11);
		mongo.save(userAccount12);
		mongo.save(userAccount13);
		mongo.save(userAccount14);
		mongo.save(userAccount15);
		mongo.save(userAccount16);
		mongo.save(userAccount17);
		mongo.save(userAccount18);

		// ----------------------------------------------------------------------------------------------
		// ------------------------------------------ PLACES --------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Place place1 = new Place("1", "Madrid", "Spain", "Pasadizo Liquidats, 107A 14ºC", "54311", "Near the cafe");
        Place place2 = new Place("2", "Sevilla", "Spain", "Pasadizo Vacuòmetres, 5A", "76123", "Near the cafe");
        Place place3 = new Place("3", "Barcelona", "Spain", "Cañada Bavarien embaldragàs, 43B", "12314", "Near the cafe");
        Place place4 = new Place("4", "Valencia", "Spain", "Pasaje Empentara, 258B 10ºB", "43214", "Near the cafe");
		Place place5 = new Place("4", "Valladolid", "Spain", "Glorieta Desomplisc torxons, 48B 19ºH", "21345", "Near the cafe");
		Place place6 = new Place("4", "Badajoz", "Pais4", "Alameda Pedraltes, 229A", "54312", "Near the cafe");

		mongo.save(place1);
        mongo.save(place2);
        mongo.save(place3);
        mongo.save(place4);
		mongo.save(place5);
		mongo.save(place6);


		// ----------------------------------------------------------------------------------------------
		// ----------------------------------------- SPEAKERS -------------------------------------------
		// ----------------------------------------------------------------------------------------------

		Actor speaker1 = new Actor()


		// ----------------------------------------------------------------------------------------------
		// --------------------------------------- ORGANIZATORS -----------------------------------------
		// ----------------------------------------------------------------------------------------------

        // ----------------------------------------------------------------------------------------------
        // -------------------------------------- SOCIAL EVENTS -----------------------------------------
        // ----------------------------------------------------------------------------------------------



        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- REGULARS -------------------------------------------
        // ----------------------------------------------------------------------------------------------




        // ----------------------------------------------------------------------------------------------
        // ------------------------------------------ GUESTS --------------------------------------------
        // ----------------------------------------------------------------------------------------------



        // ----------------------------------------------------------------------------------------------
        // ------------------------------------------ COURSES -------------------------------------------
        // ----------------------------------------------------------------------------------------------



        // ----------------------------------------------------------------------------------------------
        // -------------------------------------- ANNOUNCEMENTS -----------------------------------------
        // ----------------------------------------------------------------------------------------------






        // ----------------------------------------------------------------------------------------------
        // ---------------------------------------- MESSAGES --------------------------------------------
        // ----------------------------------------------------------------------------------------------


        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- FOLDERS --------------------------------------------
        // ----------------------------------------------------------------------------------------------


		Folder folder1 = new Folder("Inbox");
		Folder folder2 = new Folder("Outbox");
		Folder folder3 = new Folder("Trash");

		mongo.save(folder1);
		mongo.save(folder2);
		mongo.save(folder3);

		List<String> foldersString = new ArrayList<>();
		foldersString.add(folder1.getId());
		foldersString.add(folder2.getId());
		foldersString.add(folder3.getId());

		actor.setFolders(foldersString);
		mongo.save(actor);

        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- ACTORS ---------------------------------------------
        // ----------------------------------------------------------------------------------------------


        // ----------------------------------------------------------------------------------------------
        // ----------------------------------------- POSTS ----------------------------------------------
        // ----------------------------------------------------------------------------------------------



		// ----------------------------------------------------------------------------------------------
		// --------------------------------------- CONFERENCES ------------------------------------------
		// ----------------------------------------------------------------------------------------------


		// ----------------------------------------------------------------------------------------------
		// ----------------------------------------- COMMENTS -------------------------------------------
		// ----------------------------------------------------------------------------------------------

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

*/
	}
}
