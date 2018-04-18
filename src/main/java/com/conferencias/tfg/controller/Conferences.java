package com.conferencias.tfg.controller;


import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.SerializerProvider;
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
@RequestMapping("conferences")
public class Conferences {

    private ConferenceRepository conferenceRepository;
    private ActorRepository actorRepository;

	@Autowired
    public Conferences(ConferenceRepository conferenceRepository, ActorRepository actorRepository) {
        this.conferenceRepository = conferenceRepository;
        this.actorRepository = actorRepository;
    }

	@GetMapping(value = "/detailed", params = "order")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getAllDetailed(@RequestParam("order") String order) {
		List<Conference> conferences = conferenceRepository.findAll();

		if (order.equals("date"))
            conferences.sort((Conference c1, Conference c2)->parseDate(c1.getStart()).getNano()-parseDate(c2.getStart()).getNano());
        else if (order.equals("popularity"))
            conferences.sort((Conference c1, Conference c2)->c2.getPopularity().compareTo(c1.getPopularity()));
        else if (order.equals("price"))
            conferences.sort((Conference c1, Conference c2)->c1.getPrice().compareTo(c2.getPrice()));
        else
            conferences.sort((Conference c1, Conference c2)->c2.getName().compareTo(c1.getName()));

		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

    @GetMapping(value = "/short", params = "order")
    @JsonView(Short.class)
    public ResponseEntity<?> getAllShort(@RequestParam("order") String order) {
        List<Conference> conferences = conferenceRepository.findAll();

        if (order.equals("date"))
            conferences.sort((Conference c1, Conference c2)->parseDate(c1.getStart()).getNano()-parseDate(c2.getStart()).getNano());
        else if (order.equals("popularity"))
            conferences.sort((Conference c1, Conference c2)->c2.getPopularity().compareTo(c1.getPopularity()));
        else if (order.equals("price"))
            conferences.sort((Conference c1, Conference c2)->c1.getPrice().compareTo(c2.getPrice()));
        else
            conferences.sort((Conference c1, Conference c2)->c2.getName().compareTo(c1.getName()));

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    @JsonView(Detailed.class)
    public ResponseEntity<?> search(@PathVariable("keyword") String keyword) {
        List<Conference> conferences = new ArrayList<>();

        for(Conference c : conferenceRepository.findAll())
            if((c.getName() + c.getDescription() + c.getPrice().toString() + c.getSpeakersNames() + c.getTheme()).toLowerCase().contains(keyword.toLowerCase())){
                conferences.add(c);
        }

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

	@GetMapping(value = "/detailed/{idConference}")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getDetailed(@PathVariable("idConference") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

    @PutMapping("/{idConference}/{idActor}")
    public ResponseEntity<?> addOrganizator(@PathVariable("idConference") String idConferece, @PathVariable("idActor") String idActor) {

	    Actor actor = actorRepository.findOne(idActor);
	    Conference conference = conferenceRepository.findOne(idConferece);

	    if(!actor.getRole().equals("Organizator")){
	        new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

	    List<String> organizators = conference.getOrganizators();

        organizators.add(actor.getId());
        conference.setOrganizators(organizators);

        return new ResponseEntity<>(conference, HttpStatus.CREATED);
    }

	@GetMapping(value = "/short/{idConference}")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getShort(@PathVariable("idConference") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody Conference conference, UriComponentsBuilder ucBuilder) {

		if (this.conferenceExist(conference)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        conferenceRepository.save(conference);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/conference/{id}").buildAndExpand(conference.getId()).toUri());

        return new ResponseEntity<>(conference, headers, HttpStatus.CREATED);
    }

	@PutMapping(value = "/{idConference}")
	public ResponseEntity<?> edit(@PathVariable("idConference") String id, @RequestBody Conference conference) {
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

	@DeleteMapping(value = "")
	public ResponseEntity<Conference> deleteAll() {
		conferenceRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping(value = "/{idConference}")
	public ResponseEntity<?> delete(@PathVariable("idConference") String id) {
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

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
