package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Regular;
import com.conferencias.tfg.repository.RegularRepository;
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
@RequestMapping("/event/talk/regular")
public class RegularController {

    private RegularRepository regularRepository;

	@Autowired
    public RegularController(RegularRepository regularRepository) {
        this.regularRepository = regularRepository;
    }

	/** Recibe todas las charlas 'normales' */
	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAllShort() {
		List<Regular> regulars = regularRepository.findAll();
		return new ResponseEntity<Object>(regulars, HttpStatus.OK);
	}

	/** Recibe una charla 'normal' en concreto */
	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Regular regular = regularRepository.findOne(id);

		if (regular == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(regular, HttpStatus.OK);
	}

	/** Crea una charla 'normal' según los valores que se envien en el método POST */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Regular regular, UriComponentsBuilder ucBuilder) {

		if (this.regularExist(regular)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        regularRepository.save(regular);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/event/talk/regular/{id}").buildAndExpand(regular.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Modifica una charla 'normal' los campos que se indiquen */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Regular regular) {
		Regular currentRegular = regularRepository.findOne(id);

		if (currentRegular == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentRegular.setAllowedParticipants(regular.getAllowedParticipants());
		currentRegular.setDuration(regular.getDuration());
		currentRegular.setName(regular.getName());
		currentRegular.setParticipants(regular.getParticipants());
		currentRegular.setPlace(regular.getPlace());
		currentRegular.setStart(regular.getStart());
		currentRegular.setSpeakers(regular.getSpeakers());

		regularRepository.save(regular);
		return new ResponseEntity<>(regular, HttpStatus.OK);
	}

	/** Borra todos las charla 'normal' */
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Regular> deleteAll() {
		regularRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** Borra una charla 'normal' en concreto */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		Regular regular = regularRepository.findOne(id);
		if (regular == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		regularRepository.delete(id);
		return new ResponseEntity<Regular>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//
	
	/** Dada una charla 'normal', comprueba si existe una igual */
	private Boolean regularExist(Regular regular){
		Boolean res = false;

		for (Regular c : regularRepository.findAll()){
			if(regular.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
