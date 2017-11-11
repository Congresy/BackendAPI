package tfg.conferencias.gestionconferencias.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import tfg.conferencias.gestionconferencias.Domain.Actor;
import tfg.conferencias.gestionconferencias.Repository.ActorRepository;

@RestController
@RequestMapping("/actors")
@Api(value = "actors", description = "Operations pertaining to users")
public class ActorController {

	@Autowired
	private ActorRepository actorRepository;

	@ApiOperation(value = "View a list of all users", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Succesfully retrieved list"),
			@ApiResponse(code = 401, message = "Not authorized to view the resource"),
			@ApiResponse(code = 500, message = "Non-existent resource") })
	@GetMapping("/all")
	public List<Actor> getAllUsers() {
		List<Actor> actors = actorRepository.findAll();
		return actors;

	}

	@ApiOperation(value = "Search an Actor with a name", response = Actor.class)
	@GetMapping("{name}")
	public Actor getByName(@PathVariable("name") String name) {
		return actorRepository.findByName(name);
	}

	@ApiOperation(value = "Add a actor", response = Actor.class)
	@PostMapping(value = "/add")
	public Actor saveProduct(@RequestBody Actor actor) {
		return actorRepository.save(actor);

	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Update a Actor")
	@PutMapping(value = "/update/{id}")
	public Actor updateUser(@PathVariable String id, @RequestBody Actor actor) {
		Actor storedActor = actorRepository.findById(id);
		storedActor.setName(actor.getName());
		storedActor.setSurname(actor.getSurname());
		return actorRepository.save(storedActor);

	}

	@PreAuthorize(value = "hasRole('ROLE_ADMIN')")
	@ApiOperation(value = "Delete a Actor")
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity delete(@PathVariable String id) {
		actorRepository.delete(actorRepository.findById(id));
		return new ResponseEntity("Product deleted successfully", HttpStatus.OK);//

	}
}
