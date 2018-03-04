package com.conferencias.tfg.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("conference")
public class ConferenceController {

	@Autowired
	private ConferenceRepository conferenceRepository;

	@GetMapping("/allDetailed")
	@JsonView(Detailed.class)
	public List<Conference> detailed() {
		return conferenceRepository.findAll();
	}

	@GetMapping("/allShort")
	@JsonView(Shorted.class)
	public @ResponseBody List<Conference> shorted() {

		List<Conference> conferences = conferenceRepository.findAll();
		return conferences;

	}

    @PostMapping("/create")
    @JsonView(Detailed.class)
    public ResponseEntity<?> createConference(@RequestBody Conference conference, UriComponentsBuilder ucBuilder) {

        conferenceRepository.save(conference);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/conference/create/{id}").buildAndExpand(conference.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

}
