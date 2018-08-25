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
@CrossOrigin
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

    @ApiOperation(value = "Add an attendee to a certain conference")
    @PutMapping(value = "/add/{idEvent}/participants/{idActor}", produces = "application/json")
    public ResponseEntity<?> addParticipant(@PathVariable("idEvent") String idEvent, @PathVariable("idActor") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);

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

        try {
            if(event.getSeatsLeft() != 0){
                event.setSeatsLeft(event.getSeatsLeft() - 1);
            }
        } catch (Exception e){
            event.setSeatsLeft(event.getAllowedParticipants() - 1);
        }


        eventRepository.save(event);

        try {
            List<String> aux = actor.getEvents();
            aux.add(event.getId());
            actor.setEvents(aux);
            actorRepository.save(actor);
        } catch (Exception e){
            List<String> aux = new ArrayList<>();
            aux.add(event.getId());
            actor.setEvents(aux);
            actorRepository.save(actor);
        }

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete an attendee of a certain conference")
    @PutMapping(value = "/delete/{idEvent}/participants/{idActor}", produces = "application/json")
    public ResponseEntity<?> deleteParticipant(@PathVariable("idEvent") String idEvent, @PathVariable("idActor") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);

        if(actor.getRole().equals("Organizator") || actor.getRole().equals("Administrator")){
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            List<String> participants = event.getParticipants();
            participants.remove(actor.getId());
            event.setParticipants(participants);
        } catch (Exception e){
            List<String> aux = new ArrayList<>();
            aux.remove(actor.getId());
            event.setParticipants(aux);
        }

        event.setSeatsLeft(event.getSeatsLeft() + 1);
        eventRepository.save(event);

        try {
            List<String> aux = actor.getEvents();
            aux.remove(event.getId());
            actor.setEvents(aux);
            actorRepository.save(actor);
        } catch (Exception e){
            List<String> aux = new ArrayList<>();
            aux.remove(event.getId());
            actor.setEvents(aux);
            actorRepository.save(actor);
        }

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete an speaker of a certain event")
    @PutMapping("/delete/{idEvent}/speakers/{idSpeaker}")
    public ResponseEntity<?> deleteSpeaker(@PathVariable("idEvent") String idEvent, @PathVariable("idSpeaker") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);
        Conference conference = conferenceRepository.findOne(event.getConference());

        List<String> speakers = event.getSpeakers();
        speakers.remove(actor.getId());
        event.setSpeakers(speakers);

        eventRepository.save(event);

        List<String> participants = conference.getParticipants();
        participants.remove(actor.getId());
        conference.setParticipants(participants);

        conferenceRepository.save(conference);

        List<String> aux = actor.getEvents();
        aux.remove(event.getId());
        actor.setEvents(aux);

        actorRepository.save(actor);


        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Add an speaker to a certain event")
    @PutMapping("/add/{idEvent}/speakers/{idSpeaker}")
    public ResponseEntity<?> addSpeaker(@PathVariable("idEvent") String idEvent, @PathVariable("idSpeaker") String idActor) {

        Actor actor = actorRepository.findOne(idActor);
        Event event = eventRepository.findOne(idEvent);

        if(actor.getRole().equals("Organizator") || actor.getRole().equals("Administrator")){
            new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            List<String> speakers = event.getSpeakers();
            speakers.add(actor.getId());
            event.setSpeakers(speakers);
        } catch (Exception e){
            List<String> aux = new ArrayList<>();
            aux.add(actor.getId());
            event.setSpeakers(aux);
        }

        eventRepository.save(event);

        Conference conference = conferenceRepository.findOne(event.getConference());

        try {
            List<String> speakers = conference.getParticipants();
            speakers.add(actor.getId());
            conference.setParticipants(speakers);
        } catch (Exception e){
            List<String> speakers = new ArrayList<>();
            speakers.add(actor.getId());
            conference.setParticipants(speakers);
        }

        conferenceRepository.save(conference);

        try {
            List<String> aux = actor.getEvents();
            aux.add(event.getId());
            actor.setEvents(aux);
        } catch (Exception e){
            List<String> aux = new ArrayList<>();
            aux.add(event.getId());
            actor.setEvents(aux);
        }

        actorRepository.save(actor);

        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @ApiOperation(value = "List all events", response = Iterable.class)
    @GetMapping("/own/{idActor}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getEventsOfActor(@PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);
        List<Event> events = new ArrayList<>();
        List<String> eventsString = new ArrayList<>();
        List<Conference> conferences = new ArrayList<>();

        try {

            if (actor.getRole().equals("Organizator")){

                for(String s: actor.getConferences()){
                    for(Conference c : conferenceRepository.findAll()){
                        if (s.equals(c.getId())){
                            if (parseDate(c.getStart()).isAfter(LocalDateTime.now())){
                                conferences.add(c);
                            }
                        }
                    }
                }

                for (Conference c : conferences){
                    eventsString.addAll(c.getEvents());
                }

                for (String s : eventsString){
                    for (Event e : eventRepository.findAll()){
                        if(s.equals(e.getId())){
                            events.add(e);
                        }
                    }
                }

            } else {

                for (String s : actor.getEvents()){
                    for (Event e : eventRepository.findAll()){
                        if(s.equals(e.getId())){
                            events.add(e);
                        }
                    }
                }

            }

        } catch (Exception e){

            events = new ArrayList<>();

        }


        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all speakers of some event", response = Iterable.class)
    @GetMapping("/speakers/{idEvent}")
    public ResponseEntity<?> getSpeakersOfEvent(@PathVariable("idEvent") String idEvent) {
        Event event = eventRepository.findOne(idEvent);
        List<Actor> actors = new ArrayList<>();
        List<String> eventsString = new ArrayList<>();

        if (event.getSpeakers() != null){
            for (Actor sp : actorRepository.findAll()){
                if(sp.getRole().equals("Speaker") && event.getSpeakers().contains(sp.getId())){
                        actors.add(sp);
                }
            }
        }

        return new ResponseEntity<>(actors, HttpStatus.OK);
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

        Comparator<Event> comparator = Comparator.comparing(c -> parseDate(c.getStart()));

        events.sort(comparator);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @ApiOperation(value = "List all events of a certain conference, depends of users and organizators", response = Iterable.class)
    @GetMapping("/all/conferences/{idConference}/actor/{idActor}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfConferenceByUser(@PathVariable("idConference") String id, @PathVariable("idActor") String idActor) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> eventsAux = conference.getEvents();
        List<Event> events = new ArrayList<>();
        Actor actor = actorRepository.findOne(idActor);

        for(String s : eventsAux){
            if (actor.getRole().equals("User")){
                if(actor.getEvents().contains(s)) {
                    events.add(eventRepository.findOne(s));
                }
            } else {
                events.add(eventRepository.findOne(s));
            }
        }

        Comparator<Event> comparator = Comparator.comparing(c -> parseDate(c.getStart()));

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
        currentEvent.setAllowedParticipants(event.getAllowedParticipants());

		eventRepository.save(currentEvent);
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
