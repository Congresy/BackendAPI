package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Place;
import com.conferencias.tfg.repository.PlaceRepository;
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
@RequestMapping("/places")
public class Places {

    private PlaceRepository placeRepository;

	@Autowired
    public Places(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

	/** Recibe todas los lugares */
	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAllShort() {
		List<Place> places = placeRepository.findAll();
		return new ResponseEntity<Object>(places, HttpStatus.OK);
	}

	/** Recibe un lugar */
	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Place place = placeRepository.findOne(id);

		if (place == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(place, HttpStatus.OK);
	}

	/** Crea un lugar según los valores que se envien en el método POST */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Place place, UriComponentsBuilder ucBuilder) {

		if (this.placeExist(place)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        placeRepository.save(place);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/place/{id}").buildAndExpand(place.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Modifica un lugar con los campos que se indiquen */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Place place) {
		Place currentPlace = placeRepository.findOne(id);

		if (currentPlace == null) { //TODO Check si existe uno igual
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentPlace.setAddress(place.getId());
		currentPlace.setCountry(place.getId());
		currentPlace.setPostalCode(place.getId());
		currentPlace.setTown(place.getTown());

		placeRepository.save(place);
		return new ResponseEntity<>(place, HttpStatus.OK);
	}

	/** Borra todos los lugares*/
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Place> deleteAll() {
		placeRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** Borra un lugar */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		Place place = placeRepository.findOne(id);
		if (place == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		placeRepository.delete(id);
		return new ResponseEntity<Place>(HttpStatus.NO_CONTENT);
	}

    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> search(@PathVariable("keyword") String keyword) {
        List<Place> places = new ArrayList<>();

        for(Place c : placeRepository.findAll())
            if((c.getAddress() + c.getCountry() + c.getPostalCode() + c.getTown()).toLowerCase().contains(keyword.toLowerCase()))
                places.add(c);

        if (places.isEmpty()) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(places, HttpStatus.OK);
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
