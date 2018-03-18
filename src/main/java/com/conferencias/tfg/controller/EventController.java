package com.conferencias.tfg.controller;



import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.repository.EventRepository;
import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("event")
public class EventController {

    private EventRepository eventRepository;

	@Autowired
    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

	/** Recibe todas los eventos */
	@GetMapping("/all")
	public ResponseEntity<?> getAll() {
		List<Event> events = eventRepository.findAll();
		return new ResponseEntity<Object>(events, HttpStatus.OK);
	}

	/** Recibe un evento en concreto */
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> get(@PathVariable("id") long id) {
		Event event = eventRepository.findOne(id);

		if (event == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	/** Crea un evento según los valores que se envien en el método POST */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Event event, UriComponentsBuilder ucBuilder) {

		if (this.eventExist(event)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        eventRepository.save(event);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/event/create/{id}").buildAndExpand(event.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Modifica un eventocon los campos que se indiquen */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") long id, @RequestBody Event event) {
		Event currentEvent = eventRepository.findOne(id);

		if (currentEvent == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentEvent.setAllowedParticipants(event.getAllowedParticipants());
		currentEvent.setDuration(event.getDuration());
		currentEvent.setName(event.getName());
		currentEvent.setParticipants(event.getParticipants());
		currentEvent.setPlace(event.getPlace());
		currentEvent.setStart(event.getStart());
		currentEvent.setSpeakers(event.getSpeakers());

		eventRepository.save(event);
		return new ResponseEntity<>(event, HttpStatus.OK);
	}

	/** Borra todos los eventos */
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Event> deleteAll() {
		eventRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** Borra un evento en concreto */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		eventRepository.delete(id);
		return new ResponseEntity<Event>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//
	
	/** Dado un evento, comprueba si existe uno igual */
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
