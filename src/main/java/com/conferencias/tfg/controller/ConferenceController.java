package com.conferencias.tfg.controller;


import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.ConferenceRepository;
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
@RequestMapping("conference")
public class ConferenceController {

    private ConferenceRepository conferenceRepository;

	@Autowired
    public ConferenceController(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

	@GetMapping("/detailed")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getAllDetailed() {
		List<Conference> conferences = conferenceRepository.findAll();
		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

	@GetMapping("/short")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getAllShort() {
		List<Conference> conferences = conferenceRepository.findAll();
		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

	@GetMapping(value = "/detailed/{id}")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getDetailed(@PathVariable("id") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

	@GetMapping(value = "/short/{id}")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getShort(@PathVariable("id") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Conference conference, UriComponentsBuilder ucBuilder) {

		if (this.conferenceExist(conference)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        conferenceRepository.save(conference);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/conference/{id}").buildAndExpand(conference.getId()).toUri());

        return new ResponseEntity<>(conference, headers, HttpStatus.CREATED);
    }

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Conference conference) {
		Conference currentConference = conferenceRepository.findOne(id);

		if (currentConference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentConference.setEnd(conference.getEnd());
		currentConference.setName(conference.getName());
		currentConference.setStart(conference.getStart());
		currentConference.setPrice(conference.getPrice());
		currentConference.setTheme(conference.getTheme());
		currentConference.setDescription(conference.getDescription());
		currentConference.setSpeakersNames(conference.getSpeakersNames());

		conferenceRepository.save(conference);
		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete")
	public ResponseEntity<Conference> deleteAll() {
		conferenceRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		Conference conference = conferenceRepository.findOne(id);
		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		conferenceRepository.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//

	private Boolean conferenceExist(Conference conference){
		Boolean res = false;

		for (Conference c : conferenceRepository.findAll()){
			if(conference.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
