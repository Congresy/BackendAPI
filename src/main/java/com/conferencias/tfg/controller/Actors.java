package com.conferencias.tfg.controller;

import com.conferencias.tfg.configuration.CustomPasswordEncoder;
import com.conferencias.tfg.domain.*;
import com.conferencias.tfg.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("actors")
@Api(value = "Actors", description = "Operations related with actors")
public class Actors {

    private final ActorRepository actorRepository;

    private final UserAccountRepository userAccountRepository;

    private final EventRepository eventRepository;

    private final FolderRepository folderRepository;

    private final ConferenceRepository conferenceRepository;

    CustomPasswordEncoder customPasswordEncoder = new CustomPasswordEncoder();

    @Autowired
    public Actors(ActorRepository actorRepository, UserAccountRepository userAccountRepository, EventRepository eventRepository, FolderRepository folderRepository, ConferenceRepository conferenceRepository) {
        this.actorRepository = actorRepository;
        this.userAccountRepository = userAccountRepository;
        this.eventRepository = eventRepository;
        this.folderRepository = folderRepository;
        this.conferenceRepository = conferenceRepository;
    }

    @GetMapping()
    @ApiOperation("View a list of all available actors")
    public List<Actor> showAll() {
        return actorRepository.findAll();
    }

    @ApiOperation(value = "Create an actor")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody ActorWrapper actorWrapper, UriComponentsBuilder ucBuilder) {
        if (this.actorExist(actorWrapper.getActor())) {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }

        UserAccount userAccountAux = new UserAccount();
        userAccountAux.setUsername(actorWrapper.getUserAccount().getUsername());
        userAccountAux.setPassword(customPasswordEncoder.encode(actorWrapper.getUserAccount().getPassword()));
        userAccountRepository.save(userAccountAux);
        Actor aux = new Actor();
        actorWrapper.getActor().setId(aux.getId());
        actorWrapper.getActor().setBanned(false);
        actorWrapper.getActor().setPrivate_(false);
        actorWrapper.getActor().setUserAccount_(userAccountAux.getId());
        actorRepository.save(actorWrapper.getActor());

        Actor actor = actorRepository.findOne(actorWrapper.getActor().getId());

        Folder folder1 = new Folder("Inbox");
        Folder folder2 = new Folder("Outbox");
        Folder folder3 = new Folder("Trash");

        folderRepository.save(folder1);
        folderRepository.save(folder2);
        folderRepository.save(folder3);

        List<String> foldersString = new ArrayList<>();
        foldersString.add(folder1.getId());
        foldersString.add(folder2.getId());
        foldersString.add(folder3.getId());

        actor.setFolders(foldersString);
        actorRepository.save(actor);

        return new ResponseEntity<>(actorWrapper.getActor(), HttpStatus.CREATED);
    }

    @ApiOperation("View a list of followers actors")
    @GetMapping("{idActor}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<Actor> followers = new ArrayList<>();

        try {
            for (String s : actor.getFollowers()){
                followers.add(actorRepository.findOne(s));
            }
        } catch (NullPointerException e){
            followers =  new ArrayList<>();
        }

        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @ApiOperation("View a list of friends of an actor")
    @GetMapping("{idActor}/friends")
    public ResponseEntity<?> getFriends(@PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<Actor> friends = new ArrayList<>();

        try {
            for (String s : actor.getFriends()){
                friends.add(actorRepository.findOne(s));
            }
        } catch (NullPointerException e){
            friends =  new ArrayList<>();
        }

        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @ApiOperation("View a list of following actors")
    @GetMapping("{idActor}/following")
    public ResponseEntity<?> getFollowing(@PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<Actor> following = new ArrayList<>();

        try {
            for (String s : actor.getFollowing()){
                following.add(actorRepository.findOne(s));
            }
        } catch (NullPointerException e){
            following =  new ArrayList<>();
        }

        return new ResponseEntity<>(following, HttpStatus.OK);
    }

    @ApiOperation("View list of upcoming conferences of following actors")
    @GetMapping("{idActor}/upComing")
    public ResponseEntity<?> getUpcomingConferences(@PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<Conference> conferences = new ArrayList<>();

        try {
            for (String s : actor.getConferences()){
                Conference c = conferenceRepository.findOne(s);
                if (parseDate(c.getStart()).isAfter(LocalDateTime.now())){
                    conferences.add(c);
                }
            }
        } catch (NullPointerException e){
            conferences =  new ArrayList<>();
        }

        return new ResponseEntity<>(conferences, HttpStatus.OK);
    }

    @ApiOperation(value = "Follow/unfollow an actor",  response = Actor.class)
    @PutMapping(value = "/follow/{idActor}/{idActorToFollow}")
    public ResponseEntity<?> follow(@RequestParam("action") String action, @PathVariable("idActor") String idActor, @PathVariable("idActorToFollow") String idActorToFollow) {
        Actor res = actorRepository.findOne(idActor);
        Actor actor = actorRepository.findOne(idActorToFollow);
        List<String> followers;
        List<String> following;

        if (action.equals("unfollow")) {
           following = res.getFollowing();
           following.remove(actor.getId());
           res.setFollowing(following);
           actorRepository.save(res);

           followers = actor.getFollowers();
           followers.remove(res.getId());
           actor.setFollowers(followers);
           actorRepository.save(actor);

        } else if (action.equals("follow")){
            try {
                following = res.getFollowing();
                following.add(actor.getId());
                res.setFollowing(following);
                actorRepository.save(res);
            } catch (NullPointerException e){
                following = new ArrayList<>();
                following.add(actor.getId());
                res.setFollowing(following);
                actorRepository.save(res);
            }

            try {
                followers = actor.getFollowers();
                followers.add(res.getId());
                actor.setFollowers(followers);
                actorRepository.save(actor);
            } catch (NullPointerException e){
                followers = new ArrayList<>();
                followers.add(res.getId());
                actor.setFollowers(followers);
                actorRepository.save(actor);
            }
        }

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Friend/unfriend an actor",  response = Actor.class)
    @PutMapping(value = "/friend/{idActor}/{idActorToFriend}")
    public ResponseEntity<?> friend(@RequestParam("action") String action, @PathVariable("idActor") String idActor, @PathVariable("idActorToFriend") String idActorToFriend) {
        Actor actor1 = actorRepository.findOne(idActor);
        Actor actor2 = actorRepository.findOne(idActorToFriend);
        List<String> friendsOfActor1;
        List<String> friendsOfActor2;

        if (action.equals("unfriend")) {
            friendsOfActor1 = actor1.getFriends();
            friendsOfActor1.remove(actor2.getId());
            actor1.setFriends(friendsOfActor1);
            actorRepository.save(actor1);

            friendsOfActor2 = actor2.getFriends();
            friendsOfActor2.remove(actor1.getId());
            actor2.setFriends(friendsOfActor2);
            actorRepository.save(actor2);

        } else if (action.equals("friend")){
            try {
                friendsOfActor1 = actor1.getFriends();
                friendsOfActor1.add(actor2.getId());
                actor1.setFriends(friendsOfActor1);
                actorRepository.save(actor1);
            } catch (NullPointerException e){
                friendsOfActor1 = new ArrayList<>();
                friendsOfActor1.add(actor2.getId());
                actor1.setFriends(friendsOfActor1);
                actorRepository.save(actor1);
            }

            try {
                friendsOfActor2 = actor2.getFriends();
                friendsOfActor1.add(actor1.getId());
                actor2.setFriends(friendsOfActor2);
                actorRepository.save(actor2);
            } catch (NullPointerException e){
                friendsOfActor2 = new ArrayList<>();
                friendsOfActor1.add(actor1.getId());
                actor2.setFriends(friendsOfActor2);
                actorRepository.save(actor2);
            }
        }

        return new ResponseEntity<>(actor1, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get an actor by ID", response = Actor.class)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);

        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @ApiOperation(value = "Get an actor by username", response = Actor.class)
    @GetMapping(value = "/username/{username}")
    public ResponseEntity<?> get(@PathVariable("username") String username) {
        List<Actor> actors = actorRepository.findAll();
        UserAccount userAccount = null;
        Actor actor = null;

        for(Actor a : actors){
            userAccount = userAccountRepository.findOne(a.getUserAccount_());
            if(userAccount.getUsername().equals(username)){
                actor = a;
            }
        }

        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @ApiOperation(value = "Get user account of an actor by username", response = UserAccount.class)
    @GetMapping(value = "/userAccount/{username}")
    public ResponseEntity<?> getUserAccountByUsername(@PathVariable("username") String id) {
        UserAccount actor = null;

        for(UserAccount u : userAccountRepository.findAll()){
            if(u.getUsername().equals(id)){
                actor = u;
            }
        }

        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @ApiOperation(value = "Update an actor by ID")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody ActorWrapper actorWrapper) {
        Actor currentActor = actorRepository.findOne(id);

        if (currentActor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        currentActor.setName(actorWrapper.getActor().getName());
        currentActor.setSurname(actorWrapper.getActor().getSurname());
        currentActor.setEmail(actorWrapper.getActor().getEmail());
        currentActor.setPhone(actorWrapper.getActor().getPhone());
        currentActor.setPrivate_(actorWrapper.getActor().isPrivate_());
        currentActor.setPhoto(actorWrapper.getActor().getPhoto());

        userAccountRepository.findOne(currentActor.getUserAccount_()).setUsername(actorWrapper.getUserAccount().getUsername());

        userAccountRepository.save(userAccountRepository.findOne(currentActor.getUserAccount_()));

        actorRepository.save(currentActor);

        return new ResponseEntity<>(currentActor, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an actor by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);
        if (actor == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        actorRepository.delete(id);
        return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get all actors by role", response = Iterable.class)
    @GetMapping("/role/{role}")
    public ResponseEntity<?> getAllByRole(@PathVariable("role") String role) {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.getRole().equals(role) && !a.isBanned())
                actors.add(a);
        }

        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all actors by role", response = Iterable.class)
    @GetMapping("/speakers/notIn/{idEvent}")
    public ResponseEntity<?> getAllSpeakersNotInEvent(@PathVariable("idEvent") String idEvent) {
        List<Actor> actorsAux = actorRepository.findAll();
        Event event = eventRepository.findOne(idEvent);
        List<Actor> actors = new ArrayList<>();

        try {
            for (Actor a : actorsAux) {
                if (a.getRole().equals("Speaker") && !a.isBanned())
                    if (!event.getSpeakers().contains(a.getId())){
                        actors.add(a);
                    }
            }
        } catch (Exception e){
            for (Actor a : actorsAux) {
                if (a.getRole().equals("Speaker") && !a.isBanned())
                    actors.add(a);
            }
        }

        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all speakers so they can be added to an event", response = Iterable.class)
    @GetMapping("/speakers/event/{idEvent}")
    public ResponseEntity<?> getAllSpeakers(@PathVariable("idEvent") String idEvent) {
        Event event = eventRepository.findOne(idEvent);

        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.getRole().equals("Speaker") && !a.isBanned()){
                if(!event.getSpeakers().contains(a.getId())){
                    actors.add(a);
                }
            }
        }

        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @ApiOperation(value = "Ban an actor", response = Actor.class)
    @PutMapping("/banned")
    public ResponseEntity<?> ban(@RequestParam("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);

        actor.setBanned(true);

        actorRepository.save(actor);

        return new ResponseEntity<>(actor, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all actors with banned status", response = Iterable.class)
    @GetMapping("/banned")
    public ResponseEntity<?> getBanned() {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (a.isBanned()) {
                actors.add(a);
            }
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all actors by keyword", response = Iterable.class)
    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> getAllByKeyword(@PathVariable("keyword") String keyword) {
        List<Actor> actorsAux = actorRepository.findAll();
        List<Actor> actors = new ArrayList<>();
        for (Actor a : actorsAux) {
            if (!a.isBanned()){
                if ((a.getName() + a.getSurname()).toLowerCase().contains(keyword.toLowerCase())) {
                    actors.add(a);
                }
            }
        }
        return new ResponseEntity<>(actors, HttpStatus.OK);
    }


    private Boolean actorExist(Actor actor){
        Boolean res = false;

        for (Actor a: actorRepository.findAll()){
            if(actor.equals(a)){
                res = true;
                break;
            }
        }
        return res;
    }

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
