package com.conferencias.tfg.controller;


import com.conferencias.tfg.domain.*;
import com.conferencias.tfg.repository.*;
import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("conferences")
@Api(value="congresy", description="Operations pertaining to conferences in Congresy")
public class Conferences {

    private ConferenceRepository conferenceRepository;
    private ActorRepository actorRepository;
    private EventRepository eventRepository;
    private PlaceRepository placeRepository;
	private PostRepository postRepository;
	private CommentRepository commentRepository;

	@Autowired
	public Conferences(ConferenceRepository conferenceRepository, ActorRepository actorRepository, EventRepository eventRepository, PlaceRepository placeRepository, PostRepository postRepository, CommentRepository commentRepository) {
		this.conferenceRepository = conferenceRepository;
		this.actorRepository = actorRepository;
		this.eventRepository = eventRepository;
		this.placeRepository = placeRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	@ApiOperation(value = "List all system's conferences", response = Conference.class)
	@GetMapping()
	@JsonView(Detailed.class)
	public ResponseEntity<?> getAll() {
		List<Conference> conferences = conferenceRepository.findAll();

		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

    @ApiOperation(value = "List all system's conferences in detailed view", response = Conference.class)
	@GetMapping(value = "/detailed", params = "order")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getAllDetailed(@RequestParam("order") String order) {
		List<Conference> conferences = conferenceRepository.findAll();
		List<Conference> toRemove = new ArrayList<>();

		for (Conference c : conferenceRepository.findAll()){
			if (parseDate(c.getStart()).isBefore(LocalDate.now()) || c.getEvents() == null || c.getEvents().isEmpty()){
				toRemove.add(c);
			}
		}

		conferences.removeAll(toRemove);

		if (order.equals("date"))
			conferences.sort(Comparator.comparing((Conference c) -> parseDate(c.getStart())));
        else if (order.equals("price"))
            conferences.sort(Comparator.comparing(Conference::getPrice));
        else
            conferences.sort((Conference c1, Conference c2)->c2.getName().compareTo(c1.getName()));


		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

	@ApiOperation(value = "List all system's conferences in short view", response = Conference.class)
    @GetMapping(value = "/short", params = "order")
    @JsonView(Short.class)
    public ResponseEntity<?> getAllShort(@RequestParam("order") String order) {
        List<Conference> conferences = conferenceRepository.findAll();
		List<Conference> toRemove = new ArrayList<>();

		for (Conference c : conferenceRepository.findAll()){
			if (parseDate(c.getStart()).isBefore(LocalDate.now())){
				toRemove.add(c);
			}
		}

		conferences.removeAll(toRemove);

        if (order.equals("date"))
            conferences.sort(Comparator.comparing((Conference c) -> parseDate(c.getStart())));
        else if (order.equals("price"))
            conferences.sort(Comparator.comparing(Conference::getPrice));
        else
            conferences.sort((Conference c1, Conference c2)->c2.getName().compareTo(c1.getName()));

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

    @ApiOperation(value = "Search conferences by keyword", response = Iterable.class)
    @GetMapping("/search/{keyword}")
    @JsonView(Detailed.class)
    public ResponseEntity<?> search(@PathVariable("keyword") String keyword) {
        List<Conference> conferences = new ArrayList<>();
		List<Conference> toRemove = new ArrayList<>();

		for (Conference c : conferenceRepository.findAll()){
			if (parseDate(c.getStart()).isBefore(LocalDate.now())){
				toRemove.add(c);
			}
		}

        for(Conference c : conferenceRepository.findAll()){
			Place p = placeRepository.findOne(c.getPlace());
            if((c.getName() + c.getDescription() + c.getPrice().toString() + c.getSpeakersNames() + c.getTheme()).toLowerCase().contains(keyword.toLowerCase())
					|| (p.getTown() + p.getAddress() + p.getCountry() + p.getDetails() + p.getPostalCode()).toLowerCase().contains(keyword.toLowerCase())){
                conferences.add(c);
			}

        }

		conferences.removeAll(toRemove);

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
	@GetMapping(value = "/own/{idActor}")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getOwnConferences(@PathVariable("idActor") String idActor, @RequestParam("value") String value) {
		Actor actor = actorRepository.findOne(idActor);

		List<Conference> res = new ArrayList<>();

		List<String> conferences;

		try {
			conferences = actor.getConferences();
			for(String s : conferences){
				if (value.equals("upcoming")) {
					if (parseDate(conferenceRepository.findOne(s).getStart()).isAfter(LocalDate.now())){
						res.add(conferenceRepository.findOne(s));
					}
				} else if (value.equals("past")){
					if (parseDate(conferenceRepository.findOne(s).getStart()).isBefore(LocalDate.now())){
						res.add(conferenceRepository.findOne(s));
					}
				}


			}
		} catch (Exception e){
			conferences = new ArrayList<>();
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

		if(event.getAllowedParticipants() != 0){
			event.setSeatsLeft(event.getSeatsLeft() - 1);
		}

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

		currentConference.setAllowedParticipants(currentConference.getAllowedParticipants());
		currentConference.setSeatsLeft(currentConference.getSeatsLeft());
		currentConference.setParticipants(currentConference.getParticipants());
		currentConference.setEvents(currentConference.getEvents());
		currentConference.setComments(currentConference.getComments());
		currentConference.setOrganizator(currentConference.getOrganizator());
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

		// Delete events related to conference
		if (conference.getEvents() != null){
			events = conference.getEvents();

			for (String s : events){
				try {
					eventRepository.delete(eventRepository.findOne(s));
				} catch (IllegalArgumentException e){

				}

			}
		}

		// Delete comments related to conference

		// Delete the comment in the commentable element
		if (conference.getComments() != null){
			for (String idComment : conference.getComments()){
				Comment comment = commentRepository.findOne(idComment);

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

					if (a.getComments() != null){
						// Delete comment in actor
						if (a.getComments().contains(idComment)){
							List<String> commentsActor = a.getComments();
							commentsActor.remove(idComment);
							a.setComments(commentsActor);
							actorRepository.save(a);
						}
					}
				}

				// Delete responses of comment
				if (comment.getResponses() != null){
					if (comment.getResponses().isEmpty()){
						commentRepository.delete(idComment);
					} else {
						for (String s : comment.getResponses()) {
							if (s != null) {
								commentRepository.delete(commentRepository.findOne(s));
							}
						}
					}
				} else {
					commentRepository.delete(idComment);
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

    private LocalDate parseDate(String date){
		String output = date.substring(0, 10);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dt = LocalDate.parse(output, formatter);
        return dt;
    }
}
