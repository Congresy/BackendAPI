package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.repository.EventRepository;
import com.conferencias.tfg.utilities.Views;
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
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("events")
@Api(value="congresy", description="Operations pertaining to events in Congresy")
public class Events {

    private EventRepository eventRepository;
    private ConferenceRepository conferenceRepository;
    private ActorRepository actorRepository;

    @Autowired
    public Events(EventRepository eventRepository, ConferenceRepository conferenceRepository, ActorRepository actorRepository) {
        this.eventRepository = eventRepository;
        this.conferenceRepository = conferenceRepository;
        this.actorRepository = actorRepository;
    }

    @ApiOperation(value = "Add an speaker to a certain event")
    @PutMapping("/add/{idEvent}/speakers/{idSpeaker}")
    public ResponseEntity<?> addSpeaker(@PathVariable("idEvent") String idEvent, @PathVariable("idSpeaker") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);

        if(!actor.getRole().equals("Speaker")){
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            List<String> speakers = event.getSpeakers();
            speakers.add(actor.getId());
            event.setSpeakers(speakers);
            eventRepository.save(event);
        } catch (Exception e){
            List<String> aux = new ArrayList<>();
            aux.add(actor.getId());
            event.setSpeakers(aux);
            eventRepository.save(event);
        }

        //TODO

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @ApiOperation(value = "List all events", response = Iterable.class)
	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAll() {
		List<Event> events = eventRepository.findAll();
		return new ResponseEntity<>(events, HttpStatus.OK);
	}

    @ApiOperation(value = "List all events of a certain conference", response = Iterable.class)
    @GetMapping("/all/conferences/{idConference}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfConference(@PathVariable("idConference") String id) {
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

    @ApiOperation(value = "Get a certain event", response = Event.class)
	@GetMapping(value = "/{idEvent}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("idEvent") String id) {
		Event event = eventRepository.findOne(id);

		if (event == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

    @ApiOperation(value = "List all events of a specific role", response = Iterable.class)
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

    @ApiOperation(value = "List all events of certain role of certain event", response = Iterable.class)
    @GetMapping("/all/conferences/{idConference}/{role}/")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getSpecificTypeOfEventsOfConference(@PathVariable("idConference") String id, @PathVariable("role") String role) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> eventsAux = conference.getEvents();
        List<Event> events = new ArrayList<>();

        for(String s : eventsAux){
            if(eventRepository.findOne(s).getRole().equals(role))
                events.add(eventRepository.findOne(s));
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ApiOperation(value = "List all events that are talks", response = Iterable.class)
    @GetMapping("/talks/all")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getTalks() {
        List<Event> eventsAux = eventRepository.findAll();
        List<Event> events = new ArrayList<>();
        for(Event e : eventsAux)
            if(!e.getRole().equals("socialEvent"))
                events.add(e);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ApiOperation(value = "List all events that are talks of a certain conference", response = Iterable.class)
    @GetMapping("/talks/all/conferences/{idConference}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getTalksOfConference(@PathVariable("idConference") String id) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> eventsAux = conference.getEvents();
        List<Event> events = new ArrayList<>();

        for(String s : eventsAux){
            if(!eventRepository.findOne(s).getRole().equals("socialEvent"))
                events.add(eventRepository.findOne(s));
        }

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new event")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Event event, UriComponentsBuilder ucBuilder) {

		if (this.eventExist(event)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        String end;
        String start;

        if(event.getStart().contains("\\\\")){
            start = event.getStart().replaceAll("\\\\", "");
            event.setStart(start);
        }

        if(event.getEnd().contains("\\")){
            end = event.getStart().replaceAll("\\\\", "");
            event.setStart(end);
        }

        if(!event.getRole().equals("socialEvent"))
            event.setType(null);

        eventRepository.save(event);

        String conference = event.getConference();
        Conference conferenceUsed = null;

        for(Conference c : conferenceRepository.findAll()){
            if(c.getId().equals(conference)){
                conferenceUsed = c;
                try {
                    List<String> events = c.getEvents();
                    events.add(event.getId());
                    c.setEvents(events);
                } catch (Exception e){
                    List<String> events = new ArrayList<>();
                    events.add(event.getId());
                    c.setEvents(events);
                }
                conferenceRepository.save(conferenceUsed);
                break;
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/events/{idEvent}").buildAndExpand(event.getId()).toUri());

        return new ResponseEntity<>(event, headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit a certain event")
	@PutMapping(value = "/{idEvent}", produces = "application/json")
	public ResponseEntity<?> edit(@PathVariable("idEvent") String id, @RequestBody Event event) {
		Event currentEvent = eventRepository.findOne(id);

		if (currentEvent == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentEvent.setName(event.getName());
		currentEvent.setPlace(event.getPlace());
		currentEvent.setStart(event.getStart());
		currentEvent.setSpeakers(event.getSpeakers());
		currentEvent.setEnd(event.getEnd());
		currentEvent.setRequirements(event.getRequirements());
		if(currentEvent.getRole().equals("socialEvent"))
		    currentEvent.setType(event.getType());

		eventRepository.save(event);
		return new ResponseEntity<>(currentEvent, HttpStatus.OK);
	}

    @ApiOperation(value = "Delete a certain event")
	@DeleteMapping(value = "/{idEvent}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("idEvent") String idEvent) {
        Event event = eventRepository.findOne(idEvent);
        Conference conference = conferenceRepository.findOne(event.getConference());

        if (conference== null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        // TODO speakers

	    if(conference.getEvents() != null){
            List<String> newEvents = conference.getEvents();
            newEvents.remove(idEvent);
            conference.setEvents(newEvents);
            conferenceRepository.save(conference);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
