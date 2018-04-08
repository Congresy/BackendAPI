package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Comment;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Post;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.CommentRepository;
import com.conferencias.tfg.repository.ConferenceRepository;
import com.conferencias.tfg.repository.PostRepository;
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
@RequestMapping("comment")
public class CommentController {

    private CommentRepository commentRepository;
    private ConferenceRepository conferenceRepository;
    private PostRepository postRepository;
    private ActorRepository actorRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository, ConferenceRepository conferenceRepository, PostRepository postRepository, ActorRepository actorRepository) {
        this.commentRepository = commentRepository;
        this.conferenceRepository = conferenceRepository;
        this.postRepository = postRepository;
        this.actorRepository = actorRepository;
    }

	@GetMapping("/all")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAll() {
		List<Comment> comments = commentRepository.findAll();
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

    @GetMapping("/conference/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfConference(@PathVariable("id") String id) {
        Conference conference = conferenceRepository.findOne(id);
        List<String> commentsAux = conference.getComments();
        List<Comment> comments = new ArrayList<>();

        for(String s : commentsAux){
                comments.add(commentRepository.findOne(s));
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/post/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfPost(@PathVariable("id") String id) {
        Post post = postRepository.findOne(id);
        List<String> commentsAux = post.getComments();
        List<Comment> comments = new ArrayList<>();

        for(String s : commentsAux){
            comments.add(commentRepository.findOne(s));
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/actor/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfActor(@PathVariable("id") String id) {
        Actor actor = actorRepository.findOne(id);
        List<String> commentsAux = actor.getComments();
        List<Comment> comments = new ArrayList<>();

        for(String s : commentsAux){
            comments.add(commentRepository.findOne(s));
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/comment/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getResponses(@PathVariable("id") String id) {
        Comment comment = commentRepository.findOne(id);
        List<String> commentsAux = comment.getResponses();
        List<Comment> comments = new ArrayList<>();

        for(String s : commentsAux){
            comments.add(commentRepository.findOne(s));
        }

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

	@GetMapping(value = "/{id}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		Comment comment = commentRepository.findOne(id);

		if (comment == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Comment comment, UriComponentsBuilder ucBuilder) {

		if (this.commentExist(comment)) {
			return new ResponseEntity<Error>(HttpStatus.CONFLICT);
		}

        commentRepository.save(comment);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/comment/{id}").buildAndExpand(comment.getId()).toUri());

        return new ResponseEntity<>(comment, headers, HttpStatus.CREATED);
    }

    @PostMapping("/create/response/{id}")
    public ResponseEntity<?> createResponse(@PathVariable("id") String id, @RequestBody Comment comment, UriComponentsBuilder ucBuilder) {
        Comment commentToResponse = commentRepository.findOne(id);
        List<String> responses = commentToResponse.getResponses();

        commentRepository.save(comment);

        responses.add(comment.getId());
        commentToResponse.setResponses(responses);
        commentRepository.save(commentToResponse);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/comment/{id}").buildAndExpand(comment.getId()).toUri());

        return new ResponseEntity<>(comment, headers, HttpStatus.CREATED);
    }

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Comment comment) {
		Comment currentComment = commentRepository.findOne(id);

		if (currentComment == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentComment.setSentMoment(comment.getSentMoment());
		currentComment.setText(comment.getText());
		currentComment.setThumbsDown(comment.getThumbsDown());
		currentComment.setThumbsUp(comment.getThumbsUp());
		currentComment.setTitle(comment.getTitle());

		commentRepository.save(comment);
		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

	@DeleteMapping(value = "/delete/{idCommentable}/{idComment}")
	public ResponseEntity<?> delete(@PathVariable("idCommentable") String idCommentable, @PathVariable("idComment") String idComment) {
        Comment comment = commentRepository.findOne(idComment);
        Object commentable = null;

        if(postRepository.findOne(idCommentable) != null){
            commentable = postRepository.findOne(idCommentable);
            List<String> comments = ((Post) commentable).getComments();
            comments.remove(idComment);
            ((Post) commentable).setComments(comments);
            postRepository.save((Post) commentable);
        } else if (actorRepository.findOne(idCommentable) != null){
            commentable = actorRepository.findOne(idCommentable);
            List<String> comments = ((Actor) commentable).getComments();
            comments.remove(idComment);
            ((Actor) commentable).setComments(comments);
            actorRepository.save((Actor) commentable);
        } else if (conferenceRepository.findOne(idCommentable) != null){
            commentable = conferenceRepository.findOne(idCommentable);
            List<String> comments = ((Conference) commentable).getComments();
            comments.remove(idComment);
            ((Conference) commentable).setComments(comments);
            conferenceRepository.save((Conference) commentable);
        }

        if (commentable == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

		if (comment == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		commentRepository.delete(idComment);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	// ---------------------------------------------------------------------------------------------------------------//
	// ----------------------------------------------- Métodos auxiliares --------------------------------------------//
	// ---------------------------------------------------------------------------------------------------------------//

	private Boolean commentExist(Comment comment){
		Boolean res = false;

		for (Comment c : commentRepository.findAll()){
			if(comment.equals(c)){
				res = true;
				break;
			}
		}
		return res;
	}
}
