package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.repository.EventRepository;
import com.conferencias.tfg.utilities.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("event")
public class EventController {

    private EventRepository eventRepository;
    private ConferenceRepository conferenceRepository;
    private ActorRepository actorRepository;

    @Autowired
    public EventController(EventRepository eventRepository, ConferenceRepository conferenceRepository, ActorRepository actorRepository) {
        this.eventRepository = eventRepository;
        this.conferenceRepository = conferenceRepository;
        this.actorRepository = actorRepository;
    }

    @PutMapping("/add/participant/{idEvent}/{idActor}")
    public ResponseEntity<?> addParticipant(@PathVariable("idEvent") String idEvent, @PathVariable("idActor") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);

        if(actor.getRole().equals("Organizator") || actor.getRole().equals("Administrator")){
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        List<String> participants = event.getParticipants();

        participants.add(actor.getId());

        if(event.getAllowedParticipants() != 0)
            event.setAllowedParticipants(event.getAllowedParticipants()-1);

        event.setParticipants(participants);

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PutMapping("/add/speaker/{idEvent}/{idActor}")
    public ResponseEntity<?> addSpeaker(@PathVariable("idEvent") String idEvent, @PathVariable("idActor") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);

        if(!actor.getRole().equals("Speaker")){
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        List<String> speakers = event.getSpeakers();

        speakers.add(actor.getId());

        event.setSpeakers(speakers);

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAll() {
		List<Event> events = eventRepository.findAll();
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

    @GetMapping("/all/conference/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfConference(@PathVariable("id") String id) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> eventsAux = conference.getEvents();
        List<Event> events = new ArrayList<>();

        for(String s : eventsAux){
                events.add(eventRepository.findOne(s));
        }

        Comparator<Event> comparator = new Comparator<Event>() {
            @Override
            public int compare(Event c1, Event c2) {
                return parseDate(c1.getStart()).compareTo(parseDate(c2.getStart()));
            }
        };

        events.sort(comparator);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Event event = eventRepository.findOne(id);

		if (event == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

    @GetMapping("/all/{role}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getSpecificEvents(@PathVariable("role") String role) {
        List<Event> eventsAux = eventRepository.findAll();
        List<Event> events = new ArrayList<>();
        for(Event e : eventsAux)
            if(e.getRole().equals(role))
                events.add(e);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/all/conference{role}/{id}/")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getSocialEventsOfConference(@PathVariable("id") String id, @PathVariable("role") String role) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> eventsAux = conference.getEvents();
        List<Event> events = new ArrayList<>();

        for(String s : eventsAux){
            if(eventRepository.findOne(s).getRole().equals(role))
                events.add(eventRepository.findOne(s));
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/talk/all")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getTalks() {
        List<Event> eventsAux = eventRepository.findAll();
        List<Event> events = new ArrayList<>();
        for(Event e : eventsAux)
            if(!e.getRole().equals("socialEvent"))
                events.add(e);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/talk/all/conference/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getTalksOfConference(@PathVariable("id") String id) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> eventsAux = conference.getEvents();
        List<Event> events = new ArrayList<>();

        for(String s : eventsAux){
            if(!eventRepository.findOne(s).getRole().equals("socialEvent"))
                events.add(eventRepository.findOne(s));
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Event event, UriComponentsBuilder ucBuilder) {

		if (this.eventExist(event)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        if(!event.getRole().equals("socialEvent"))
            event.setType(null);

        eventRepository.save(event);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/event/{id}").buildAndExpand(event.getId()).toUri());

        return new ResponseEntity<>(event, headers, HttpStatus.CREATED);
    }

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Event event) {
		Event currentEvent = eventRepository.findOne(id);

		if (currentEvent == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentEvent.setAllowedParticipants(event.getAllowedParticipants());
		currentEvent.setName(event.getName());
		currentEvent.setParticipants(event.getParticipants());
		currentEvent.setPlace(event.getPlace());
		currentEvent.setStart(event.getStart());
		currentEvent.setSpeakers(event.getSpeakers());
		currentEvent.setEnd(event.getEnd());
		currentEvent.setRequirements(event.getRequirements());
		if(currentEvent.getRole().equals("socialEvent"))
		    currentEvent.setType(event.getType());

		eventRepository.save(event);
		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity<Event> deleteAll() {
		eventRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(value = "/delete/{idConference}/{idEvent}")
	public ResponseEntity<?> delete(@PathVariable("idConference") String idConference, @PathVariable("idEvent") String idEvent) {
        Event event = eventRepository.findOne(idEvent);
        Conference conference = conferenceRepository.findOne(idConference);

        if (conference== null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

	    List<String> newEvents = conference.getEvents();
	    newEvents.remove(idEvent);
	    conference.setEvents(newEvents);
	    conferenceRepository.save(conference);

		if (event == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		eventRepository.delete(idEvent);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//

	private Boolean eventExist(Event event){
		Boolean res = false;

		for (Event c : eventRepository.findAll()){
			if(event.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
