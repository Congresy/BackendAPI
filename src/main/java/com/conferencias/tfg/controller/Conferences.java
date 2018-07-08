package com.conferencias.tfg.controller;


import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.domain.UserAccount;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.repository.EventRepository;
import com.conferencias.tfg.repository.UserAccountRepository;
import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
@RequestMapping("conferences")
@Api(value="congresy", description="Operations pertaining to conferences in Congresy")
public class Conferences {

    private ConferenceRepository conferenceRepository;
    private ActorRepository actorRepository;
    private UserAccountRepository userAccountRepository;
    private EventRepository eventRepository;

	@Autowired
    public Conferences(ConferenceRepository conferenceRepository, ActorRepository actorRepository, UserAccountRepository userAccountRepository, EventRepository eventRepository) {
        this.conferenceRepository = conferenceRepository;
        this.actorRepository = actorRepository;
        this.userAccountRepository = userAccountRepository;
        this.eventRepository = eventRepository;
    }

    @ApiOperation(value = "List all system's conferences in detailed view", response = Conference.class)
	@GetMapping(value = "/detailed", params = "order")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getAllDetailed(@RequestParam("order") String order) {
		List<Conference> conferences = conferenceRepository.findAll();

		if (order.equals("date"))
            conferences.sort((Conference c1, Conference c2)->parseDate(c1.getStart()).getNano()-parseDate(c2.getStart()).getNano());
        else if (order.equals("popularity"))
            conferences.sort((Conference c1, Conference c2)->c2.getPopularity().compareTo(c1.getPopularity()));
        else if (order.equals("price"))
            conferences.sort((Conference c1, Conference c2)->c1.getPrice().compareTo(c2.getPrice()));
        else
            conferences.sort((Conference c1, Conference c2)->c2.getName().compareTo(c1.getName()));

		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

    @ApiOperation(value = "List all system's conferences in short view", response = Conference.class)
    @GetMapping(value = "/short", params = "order")
    @JsonView(Short.class)
    public ResponseEntity<?> getAllShort(@RequestParam("order") String order) {
        List<Conference> conferences = conferenceRepository.findAll();

        if (order.equals("date"))
            conferences.sort((Conference c1, Conference c2)->parseDate(c1.getStart()).getNano()-parseDate(c2.getStart()).getNano());
        else if (order.equals("popularity"))
            conferences.sort((Conference c1, Conference c2)->c2.getPopularity().compareTo(c1.getPopularity()));
        else if (order.equals("price"))
            conferences.sort((Conference c1, Conference c2)->c1.getPrice().compareTo(c2.getPrice()));
        else
            conferences.sort((Conference c1, Conference c2)->c2.getName().compareTo(c1.getName()));

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

    @ApiOperation(value = "Search conferences by keyword", response = Iterable.class)
    @GetMapping("/search/{keyword}")
    @JsonView(Detailed.class)
    public ResponseEntity<?> search(@PathVariable("keyword") String keyword) {
        List<Conference> conferences = new ArrayList<>();

        for(Conference c : conferenceRepository.findAll())
            if((c.getName() + c.getDescription() + c.getPrice().toString() + c.getSpeakersNames() + c.getTheme()).toLowerCase().contains(keyword.toLowerCase())){
                conferences.add(c);
        }

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

    @ApiOperation(value = "Get certain conference in detailed view", response = Conference.class)
	@GetMapping(value = "/detailed/{idConference}")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getDetailed(@PathVariable("idConference") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

	@ApiOperation(value = "Get conferences of an organizator by username", response = Iterable.class)
	@GetMapping(value = "/organizator/{username}")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getOwnConferences(@PathVariable("username") String username) {
		List<Actor> actors = actorRepository.findAll();
		UserAccount userAccount;
		Actor actor = null;

		for(Actor a : actors){
			userAccount = userAccountRepository.findOne(a.getUserAccount_());
			if(userAccount.getUsername().equals(username)){
				actor = a;
			}
		}

		List<Conference> res = new ArrayList<>();

		List<String> conferences = new ArrayList<>();
		try {
			conferences = actor.getConferences();
			for(String s : conferences){
				res.add(conferenceRepository.findOne(s));
			}
		} catch (Exception e){
			res = new ArrayList<>();
		}

		return new ResponseEntity<>(res, HttpStatus.OK);
	}

    @ApiOperation(value = "Get a certain conference in short view", response = Conference.class)
	@GetMapping(value = "/short/{idConference}")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getShort(@PathVariable("idConference") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}


	@ApiOperation(value = "Add an attendee to a certain conference")
	@PutMapping(value = "/add/{idConference}/participants/{idActor}", produces = "application/json")
	public ResponseEntity<?> addParticipant(@PathVariable("idConference") String idEvent, @PathVariable("idActor") String idActor) {

		Actor actor = actorRepository.findOne(idActor);
		Conference event = conferenceRepository.findOne(idEvent);

		if(actor.getRole().equals("Organizator") || actor.getRole().equals("Administrator")){
			new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}

		try {
			List<String> participants = event.getParticipants();
			participants.add(actor.getId());
			event.setParticipants(participants);
		} catch (Exception e){
			List<String> aux = new ArrayList<>();
			aux.add(actor.getId());
			event.setParticipants(aux);
		}

		if(event.getAllowedParticipants() != 0)
			event.setAllowedParticipants(event.getAllowedParticipants()-1);
		conferenceRepository.save(event);

		try {
			List<String> aux = actor.getConferences();
			aux.add(event.getId());
			actor.setConferences(aux);
			actorRepository.save(actor);
		} catch (Exception e){
			List<String> aux = new ArrayList<>();
			aux.add(event.getId());
			actor.setConferences(aux);
			actorRepository.save(actor);
		}

		return new ResponseEntity<>(event, HttpStatus.CREATED);
	}

    @ApiOperation(value = "Create a new conference")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Conference conference, UriComponentsBuilder ucBuilder) {

	    String end;
	    String start;

		if (this.conferenceExist(conference)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

		if(conference.getStart().contains("\\\\")){
		    start = conference.getStart().replaceAll("\\\\", "");
		    conference.setStart(start);
        }

        if(conference.getEnd().contains("\\")){
            end = conference.getStart().replaceAll("\\\\", "");
            conference.setStart(end);
        }

        String organizator = conference.getOrganizator();
		List<Actor> actors = actorRepository.findAll();
		Actor actor = null;

        conferenceRepository.save(conference);

		for(Actor a : actors){
			if(a.getUserAccount_().equals(organizator)){
				actor = a;
			}
		}

		try {
			List<String> aux = actor.getConferences();
			aux.add(conference.getId());
			actor.setConferences(aux);
			actorRepository.save(actor);
		} catch (Exception e){
			List<String> aux = new ArrayList<>();
			aux.add(conference.getId());
			actor.setConferences(aux);
			actorRepository.save(actor);
		}

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/conference/{id}").buildAndExpand(conference.getId()).toUri());

        return new ResponseEntity<>(conference, headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit a certain conference")
	@PutMapping(value = "/{idConference}", produces = "application/json")
	public ResponseEntity<?> edit(@PathVariable("idConference") String id, @RequestBody Conference conference) {
		Conference currentConference = conferenceRepository.findOne(id);

		if (currentConference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentConference.setAllowedParticipants(conference.getAllowedParticipants());
		currentConference.setEnd(conference.getEnd());
		currentConference.setName(conference.getName());
		currentConference.setStart(conference.getStart());
		currentConference.setPrice(conference.getPrice());
		currentConference.setTheme(conference.getTheme());
		currentConference.setDescription(conference.getDescription());
		currentConference.setSpeakersNames(conference.getSpeakersNames());

		conferenceRepository.save(currentConference);
		return new ResponseEntity<>(currentConference, HttpStatus.OK);
	}

    @ApiOperation(value = "Delete a certain conference")
	@DeleteMapping(value = "/{idConference}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("idConference") String id) {
		Conference conference = conferenceRepository.findOne(id);
		List<String> events = new ArrayList<>();

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		//TODO Delete events of speakers

		// Delete events related to conference
		if (conference.getEvents() != null){
			events = conference.getEvents();

			for (String s : events){
				Event e = eventRepository.findOne(s);
				eventRepository.delete(e);
			}
		}

		// Delete form actors
		for (Actor a : actorRepository.findAll()){
			if (a.getConferences() != null){
				if (a.getConferences().contains(conference.getId())){
					List<String> conferences = a.getConferences();
					conferences.remove(conference.getId());
					a.setConferences(conferences);
					actorRepository.save(a);
				}
			}
		}

		conferenceRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//

	private Boolean conferenceExist(Conference conference){
		Boolean res = false;

		for (Conference c : conferenceRepository.findAll()){
			if(conference.equals(c)){
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
