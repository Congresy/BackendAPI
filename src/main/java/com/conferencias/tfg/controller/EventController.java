package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Event;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("event")
public class EventController {

    private EventRepository eventRepository;
    private ConferenceRepository conferenceRepository;

	@Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Event event = eventRepository.findOne(id);
	    List<Conference> conferences = conferenceRepository.findAll();

	    for(Conference c : conferences) {
            if (c.getEvents().contains(id)) {
                List<String> newEvents = c.getEvents();
                newEvents.remove(id);
                c.setEvents(newEvents);
                conferenceRepository.save(c);
            }
        }

		if (event == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		eventRepository.delete(id);
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
}
