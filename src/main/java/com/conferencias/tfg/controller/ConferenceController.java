package com.conferencias.tfg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.utilities.Views.Detailed;
import com.conferencias.tfg.utilities.Views.Shorted;
import com.fasterxml.jackson.annotation.JsonView;

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

}
