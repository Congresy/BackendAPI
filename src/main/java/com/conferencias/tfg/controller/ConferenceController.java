package com.conferencias.tfg.controller;


import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Comment;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Message;
import com.conferencias.tfg.repository.ActorRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("conference")
public class ConferenceController {

    private ConferenceRepository conferenceRepository;
    private ActorRepository actorRepository;

	@Autowired
    public ConferenceController(ConferenceRepository conferenceRepository, ActorRepository actorRepository) {
        this.conferenceRepository = conferenceRepository;
        this.actorRepository = actorRepository;
    }

	@GetMapping("/detailed/date")
	@JsonView(Detailed.class)
	public ResponseEntity<?> getAllDetailedByDate() {
		List<Conference> conferences = conferenceRepository.findAll();

        Comparator<Conference> comparator = new Comparator<Conference>() {
            @Override
            public int compare(Conference c1, Conference c2) {
                return parseDate(c1.getStart()).compareTo(parseDate(c2.getStart()));
            }
        };

        conferences.sort(comparator);

		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

    @GetMapping("/detailed/search/{keyword}")
    @JsonView(Detailed.class)
    public ResponseEntity<?> getAllDetailedByDate(@PathVariable("keyword") String keyword) {
        List<Conference> conferences = new ArrayList<>();

        for(Conference c : conferenceRepository.findAll())
            if((c.getName() + c.getDescription() + c.getPrice().toString() + c.getSpeakersNames() + c.getTheme()).toLowerCase().contains(keyword.toLowerCase())){
                conferences.add(c);
        }

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

    @GetMapping("/shorted/search/{keyword}")
    @JsonView(Shorted.class)
    public ResponseEntity<?> getAllShortedByDate(@PathVariable("keyword") String keyword) {
        List<Conference> conferences = new ArrayList<>();

        for(Conference c : conferenceRepository.findAll())
            if((c.getName() + c.getDescription() + c.getPrice().toString() + c.getSpeakersNames() + c.getTheme()).toLowerCase().contains(keyword.toLowerCase())){
                conferences.add(c);
            }

        return new ResponseEntity<Object>(conferences, HttpStatus.OK);
    }

	@GetMapping("/short")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getAllShort() {
		List<Conference> conferences = conferenceRepository.findAll();
		return new ResponseEntity<Object>(conferences, HttpStatus.OK);
	}

    @GetMapping("/short/date")
    @JsonView(Shorted.class)
    public ResponseEntity<?> getAllShortByDate() {
        List<Conference> conferences = conferenceRepository.findAll();

        Comparator<Conference> comparator = new Comparator<Conference>() {
            @Override
            public int compare(Conference c1, Conference c2) {
                return parseDate(c1.getStart()).compareTo(parseDate(c2.getStart()));
            }
        };

        conferences.sort(comparator);

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

    @PutMapping("/add/{idConference}/{idActor}")
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

	@GetMapping(value = "/short/{id}")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getShort(@PathVariable("id") String id) {
		Conference conference = conferenceRepository.findOne(id);

		if (conference == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(conference, HttpStatus.OK);
	}

    @GetMapping(value = "/all/popularity")
    @JsonView(Detailed.class)
    public ResponseEntity<?> getAllByPopularity() {
        List<Conference> conferences = conferenceRepository.findAll();

        if (conferences.isEmpty()) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        Comparator<Conference> comparator = new Comparator<Conference>() {
            @Override
            public int compare(Conference c1, Conference c2) {
                return c1.getPopularity().compareTo(c2.getPopularity());
            }
        };

        conferences.sort(comparator);

        return new ResponseEntity<>(conferences, HttpStatus.OK);
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

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
