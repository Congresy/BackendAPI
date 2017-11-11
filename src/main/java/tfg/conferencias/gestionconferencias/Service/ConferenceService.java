package tfg.conferencias.gestionconferencias.Service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import tfg.conferencias.gestionconferencias.Domain.Conference;
import tfg.conferencias.gestionconferencias.Repository.ConferenceRepository;

@Service
public class ConferenceService {

	@Autowired
	private ConferenceRepository conferenceRepository;

	public Collection<Conference> findAll() {
		return this.conferenceRepository.findAll();
	}
	
	public Conference save(Conference conference) {
		Assert.notNull(conference);
		return this.conferenceRepository.save(conference);
		//
	}
}
