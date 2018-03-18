package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Course;
import com.conferencias.tfg.repository.CourseRepository;
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
@RequestMapping("/event/talk/course")
public class CourseController {

    private CourseRepository courseRepository;

	@Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

	/** Recibe todas los cursos */
	@GetMapping("/all")
	@JsonView(Shorted.class)
	public ResponseEntity<?> getAllShort() {
		List<Course> courses = courseRepository.findAll();
		return new ResponseEntity<Object>(courses, HttpStatus.OK);
	}

	/** Recibe un curso en concreto */
	@GetMapping(value = "/{id}")
	@JsonView(Shorted.class)
	public ResponseEntity<?> get(@PathVariable("id") long id) {
		Course course = courseRepository.findOne(id);

		if (course == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	/** Crea un curso según los valores que se envien en el método POST */
    @PostMapping("/create")
    @JsonView(Detailed.class)
    public ResponseEntity<?> create(@RequestBody Course course, UriComponentsBuilder ucBuilder) {

		if (this.courseExist(course)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        courseRepository.save(course);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/course/create/{id}").buildAndExpand(course.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /** Modifica un curso según los campos que se indiquen */
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") long id, @RequestBody Course course) {
		Course currentCourse = courseRepository.findOne(id);

		if (currentCourse == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentCourse.setAllowedParticipants(course.getAllowedParticipants());
		currentCourse.setDuration(course.getDuration());
		currentCourse.setName(course.getName());
		currentCourse.setParticipants(course.getParticipants());
		currentCourse.setPlace(course.getPlace());
		currentCourse.setStart(course.getStart());
		currentCourse.setSpeakers(course.getSpeakers());

		courseRepository.save(course);
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	/** Borra todos los cursos */
	@DeleteMapping(value = "/delete")
	public ResponseEntity<Course> deleteAll() {
		courseRepository.deleteAll();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/** Borra a un curso en concreto */
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		Course course = courseRepository.findOne(id);
		if (course == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}
		courseRepository.delete(id);
		return new ResponseEntity<Course>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//
	
	/** Dado un curso, comprueba si existe uno igual */
	private Boolean courseExist(Course course){
		Boolean res = false;

		for (Course c : courseRepository.findAll()){
			if(course.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
