package tfg.conferencias.gestionconferencias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tfg.conferencias.gestionconferencias.Domain.Conference;
import tfg.conferencias.gestionconferencias.Service.ConferenceService;

@RestController
@RequestMapping("/conferences")
public class ConferenceController {

	@Autowired
	private ConferenceService conferenceService;

	@GetMapping("/all")
	public List<Conference> getAll() {
		List<Conference> conferences = (List) conferenceService.findAll();
		return conferences;
	}
	
	@PostMapping("")
	public Conference create(@RequestBody Conference conference) {
		return this.conferenceService.save(conference);
		
	}
}
