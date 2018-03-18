package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.SocialEvent;
import com.conferencias.tfg.repository.SocialEventRepository;
import com.conferencias.tfg.utilities.Views;
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
@RequestMapping("/event/socialEvent")
public class SocialEventController {

    private SocialEventRepository socialEventRepository;

	@Autowired
    public SocialEventController(SocialEventRepository socialEventRepository) {
        this.socialEventRepository = socialEventRepository;
    }

	/** Recibe todas los eventos sociales */
	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAllShort() {
		List<SocialEvent> socialEvents = socialEventRepository.findAll();
		return new ResponseEntity<Object>(socialEvents, HttpStatus.OK);
	}

	/** Recibe un evento social en concreto */
	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") long id) {
		SocialEvent socialEvent = socialEventRepository.findOne(id);

		if (socialEvent == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(socialEvent, HttpStatus.OK);
	}

	/** Crea un evento social según los valores que se envien en el método POST */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody SocialEvent socialEvent, UriComponentsBuilder ucBuilder) {

		if (this.socialEventExist(socialEvent)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        socialEventRepository.save(socialEvent);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/event/socialEvent/create/{id}").buildAndExpand(socialEvent.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Modifica un evento social los campos que se indiquen */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") long id, @RequestBody SocialEvent socialEvent) {
		SocialEvent currentSocialEvent = socialEventRepository.findOne(id);

		if (currentSocialEvent == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentSocialEvent.setAllowedParticipants(socialEvent.getAllowedParticipants());
		currentSocialEvent.setDuration(socialEvent.getDuration());
		currentSocialEvent.setName(socialEvent.getName());
		currentSocialEvent.setParticipants(socialEvent.getParticipants());
		currentSocialEvent.setPlace(socialEvent.getPlace());
		currentSocialEvent.setStart(socialEvent.getStart());
		currentSocialEvent.setSpeakers(socialEvent.getSpeakers());

		socialEventRepository.save(socialEvent);
		return new ResponseEntity<>(socialEvent, HttpStatus.OK);
	}

	/** Borra todos los eventos sociales */
	@DeleteMapping(value = "/delete")
	public ResponseEntity<SocialEvent> deleteAll() {
		socialEventRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** Borra a un evento social en concreto */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		SocialEvent socialEvent = socialEventRepository.findOne(id);
		if (socialEvent == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		socialEventRepository.delete(id);
		return new ResponseEntity<SocialEvent>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//
	
	/** Dada un evento social, comprueba si existe uno igual */
	private Boolean socialEventExist(SocialEvent socialEvent){
		Boolean res = false;

		for (SocialEvent c : socialEventRepository.findAll()){
			if(socialEvent.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
