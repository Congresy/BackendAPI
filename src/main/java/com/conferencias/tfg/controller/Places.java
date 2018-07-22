package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Event;
import com.conferencias.tfg.domain.Place;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.repository.EventRepository;
import com.conferencias.tfg.repository.PlaceRepository;
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

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/places")
@Api(value="congresy", description="Operations pertaining to places in Congresy")
public class Places {

    private PlaceRepository placeRepository;
    private ActorRepository actorRepository;
    private ConferenceRepository conferenceRepository;
    private EventRepository eventRepository;

	@Autowired
    public Places(PlaceRepository placeRepository, ActorRepository actorRepository, ConferenceRepository conferenceRepository, EventRepository eventRepository) {
        this.placeRepository = placeRepository;
        this.actorRepository = actorRepository;
        this.conferenceRepository = conferenceRepository;
        this.eventRepository = eventRepository;
    }

	/** Recibe un lugar */
    @ApiOperation(value = "Get a certain place", response = Place.class)
	@GetMapping(value = "/{idPlace}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("idPlace") String id) {
		Place place = placeRepository.findOne(id);

		if (place == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(place, HttpStatus.OK);
	}

	/** Crea un lugar según los valores que se envien en el método POST */
    @ApiOperation(value = "Create a new place depending of the id object")
    @PostMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> create(@PathVariable("id") String id, @RequestBody Place place) {

		if (this.placeExist(place)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

		placeRepository.save(place);

		if (actorRepository.findOne(id) != null){
			Actor actor = actorRepository.findOne(id);
			actor.setPlace(place.getId());
			actorRepository.save(actor);
		} else if (conferenceRepository.findOne(id) != null){
			Conference conference = conferenceRepository.findOne(id);
			conference.setPlace(place.getId());
			conferenceRepository.save(conference);
		} else if (eventRepository.findOne(id) != null){
			Event event = eventRepository.findOne(id);
			event.setPlace(place.getId());{
				eventRepository.save(event);
			}
		}

        return new ResponseEntity<>(place, HttpStatus.CREATED);
    }

    /** Modifica un lugar con los campos que se indiquen */
    @ApiOperation(value = "Edit a certain place")
	@PutMapping(value = "/{idPlace}", produces = "application/json")
	public ResponseEntity<?> edit(@PathVariable("idPlace") String id, @RequestBody Place place) {
		Place currentPlace = placeRepository.findOne(id);

		if (currentPlace == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentPlace.setAddress(place.getAddress());
		currentPlace.setCountry(place.getCountry());
		currentPlace.setPostalCode(place.getPostalCode());
		currentPlace.setTown(place.getTown());
		currentPlace.setDetails(place.getDetails());

		placeRepository.save(currentPlace);

		return new ResponseEntity<>(currentPlace, HttpStatus.OK);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//
	
	/** Dada un lugar, comprueba si existe uno igual */
	private Boolean placeExist(Place place){
		Boolean res = false;

		for (Place c : placeRepository.findAll()){
			if(place.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
