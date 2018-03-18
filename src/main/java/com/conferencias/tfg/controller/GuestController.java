package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Guest;
import com.conferencias.tfg.repository.GuestRepository;
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
@RequestMapping("/event/talk/guest")
public class GuestController {

    private GuestRepository guestRepository;

	@Autowired
    public GuestController(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

	/** Recibe todas las charlas de invitados */
	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAllShort() {
		List<Guest> guests = guestRepository.findAll();
		return new ResponseEntity<Object>(guests, HttpStatus.OK);
	}

	/** Recibe una charla de invitados en concreto */
	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Guest guest = guestRepository.findOne(id);

		if (guest == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(guest, HttpStatus.OK);
	}

	/** Crea una charla de invitados según los valores que se envien en el método POST */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Guest guest, UriComponentsBuilder ucBuilder) {

		if (this.guestExist(guest)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        guestRepository.save(guest);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/event/talk/guest/{id}").buildAndExpand(guest.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Modifica una charla de invitados los campos que se indiquen */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Guest guest) {
		Guest currentGuest = guestRepository.findOne(id);

		if (currentGuest == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentGuest.setAllowedParticipants(guest.getAllowedParticipants());
		currentGuest.setDuration(guest.getDuration());
		currentGuest.setName(guest.getName());
		currentGuest.setParticipants(guest.getParticipants());
		currentGuest.setPlace(guest.getPlace());
		currentGuest.setStart(guest.getStart());
		currentGuest.setSpeakers(guest.getSpeakers());

		guestRepository.save(guest);
		return new ResponseEntity<>(guest, HttpStatus.OK);
	}

	/** Borra todas las charlas de invitados */
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Guest> deleteAll() {
		guestRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** Borra una charla de invitados en concreto */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		Guest guest = guestRepository.findOne(id);
		if (guest == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		guestRepository.delete(id);
		return new ResponseEntity<Guest>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//
	
	/** Dada una charla de invitados, comprueba si existe uno igual */
	private Boolean guestExist(Guest guest){
		Boolean res = false;

		for (Guest c : guestRepository.findAll()){
			if(guest.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
