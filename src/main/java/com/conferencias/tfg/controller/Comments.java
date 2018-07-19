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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("comments")
@Api(value="congresy", description="Operations pertaining to comments in Congresy")
public class Comments {

    private CommentRepository commentRepository;
    private ConferenceRepository conferenceRepository;
    private PostRepository postRepository;
    private ActorRepository actorRepository;

    @Autowired
    public Comments(CommentRepository commentRepository, ConferenceRepository conferenceRepository, PostRepository postRepository, ActorRepository actorRepository) {
        this.commentRepository = commentRepository;
        this.conferenceRepository = conferenceRepository;
        this.postRepository = postRepository;
        this.actorRepository = actorRepository;
    }

    @ApiOperation(value = "List all of system's comments", response = Iterable.class)
	@GetMapping()
	@JsonView(Views.Default.class)
	public ResponseEntity<?> getAll() {
		List<Comment> comments = commentRepository.findAll();
		return new ResponseEntity<>(comments, HttpStatus.OK);
	}

    @ApiOperation(value = "Search a comments in a commentable element by keyword", response = Iterable.class)
    @GetMapping("/commentable/{idCommentable}/search/{keyword}/")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> searchInCommentable(@PathVariable("idCommentable") String commentableId, @PathVariable("keyword") String keyword) {
        List<Comment> comments = new ArrayList<>();
        List<String> commentsAux = new ArrayList<>();
        Object commentable = null;

        if(postRepository.findOne(commentableId) != null){
            commentable = postRepository.findOne(commentableId);
            commentsAux = ((Post) commentable).getComments();
        } else if (actorRepository.findOne(commentableId) != null){
            commentable = actorRepository.findOne(commentableId);
            commentsAux = ((Actor) commentable).getComments();
        } else if (conferenceRepository.findOne(commentableId) != null){
            commentable = conferenceRepository.findOne(commentableId);
            commentsAux = ((Conference) commentable).getComments();
        }

        if (commentable == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        for(String s : commentsAux){
            if(commentRepository.findOne(s).getText().toLowerCase().contains(keyword.toLowerCase()) || commentRepository.findOne(s).getTitle().toLowerCase().contains(keyword.toLowerCase()))
                comments.add(commentRepository.findOne(s));
        }

        if (comments.isEmpty()) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @ApiOperation(value = "List all the comments of a certain commentable element", response = Iterable.class)
    @GetMapping("/commentable/{idCommentable}")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getAllOfCommentable(@PathVariable("idCommentable") String id) {
        List<Comment> comments = new ArrayList<>();
        List<String> commentsAux = new ArrayList<>();
        Object commentable = null;

        if(postRepository.findOne(id) != null){
            commentable = postRepository.findOne(id);
            commentsAux = ((Post) commentable).getComments();
        } else if (actorRepository.findOne(id) != null){
            commentable = actorRepository.findOne(id);
            commentsAux = ((Actor) commentable).getComments();
        } else if (conferenceRepository.findOne(id) != null){
            commentable = conferenceRepository.findOne(id);
            commentsAux = ((Conference) commentable).getComments();
        }

        if (commentable == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        for(String s : commentsAux){
                comments.add(commentRepository.findOne(s));
        }

        Comparator<Comment> comparator = new Comparator<Comment>() {
            @Override
            public int compare(Comment c1, Comment c2) {
                Integer c1T = c1.getThumbsUp() - c1.getThumbsDown();
                Integer c2T = c2.getThumbsUp() - c2.getThumbsDown();
                if(c1T.compareTo(c2T) == 0)
                    return parseDate(c1.getSentMoment()).compareTo(parseDate(c2.getSentMoment()));
                else {
                    return c1T.compareTo(c2T);
                }
            }
        };

        comments.sort(comparator);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @ApiOperation(value = "List all of the responses of a certain comment", response = Iterable.class)
    @GetMapping("/{idComment}/responses")
    @JsonView(Views.Default.class)
    public ResponseEntity<?> getResponses(@PathVariable("idComment") String id) {
        Comment comment = commentRepository.findOne(id);
        List<String> commentsAux = comment.getResponses();
        List<Comment> comments = new ArrayList<>();

        for(String s : commentsAux){
            comments.add(commentRepository.findOne(s));
        }

        Comparator<Comment> comparator = new Comparator<Comment>() {
            @Override
            public int compare(Comment c1, Comment c2) {
                return parseDate(c1.getSentMoment()).compareTo(parseDate(c2.getSentMoment()));
            }
        };

        comments.sort(comparator);

        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a certain comment", response = Comment.class)
	@GetMapping(value = "/{commentId}")
	@JsonView(Views.Default.class)
	public ResponseEntity<?> get(@PathVariable("commentId") String id) {
		Comment comment = commentRepository.findOne(id);

		if (comment == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(comment, HttpStatus.OK);
	}

    @ApiOperation(value = "Create a new comment")
    @PostMapping(value="/{idCommentable}/{idAuthor}", produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Comment comment, @PathVariable("idAuthor") String idAuthor, @PathVariable("idCommentable") String idCommentable) {

        List<String> commentsAux;
        Object commentable;
        Actor actor = actorRepository.findOne(idAuthor);
        List<String> commentsActor;

        commentRepository.save(comment);

        try {
            commentable = postRepository.findOne(idCommentable);

            try {
                commentsAux = ((Post) commentable).getComments();
                commentsAux.add(comment.getId());
                ((Post) commentable).setComments(commentsAux);
                postRepository.save((Post)commentable);

                comment.setCommentable(((Post) commentable).getId());
                commentRepository.save(comment);

                try {
                    commentsActor = actor.getComments();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                } catch (NullPointerException e1){
                    commentsActor = new ArrayList<>();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                }

            } catch (NullPointerException e){
                commentsAux = new ArrayList<>();
                commentsAux.add(comment.getId());
                ((Post) commentable).setComments(commentsAux);
                postRepository.save((Post)commentable);

                comment.setCommentable(((Post) commentable).getId());
                commentRepository.save(comment);

                try {
                    commentsActor = actor.getComments();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                } catch (NullPointerException e1){
                    commentsActor = new ArrayList<>();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                }

            }

        } catch(NullPointerException e){
            commentable = conferenceRepository.findOne(idCommentable);

            try {
                commentsAux = ((Conference) commentable).getComments();
                commentsAux.add(comment.getId());
                ((Conference) commentable).setComments(commentsAux);
                conferenceRepository.save((Conference)commentable);

                comment.setCommentable(((Conference) commentable).getId());
                commentRepository.save(comment);

                try {
                    commentsActor = actor.getComments();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                } catch (NullPointerException e1){
                    commentsActor = new ArrayList<>();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                }

            } catch (NullPointerException n){
                commentsAux = new ArrayList<>();
                commentsAux.add(comment.getId());
                ((Conference) commentable).setComments(commentsAux);
                conferenceRepository.save((Conference)commentable);

                comment.setCommentable(((Conference) commentable).getId());
                commentRepository.save(comment);

                try {
                    commentsActor = actor.getComments();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                } catch (NullPointerException e1){
                    commentsActor = new ArrayList<>();
                    commentsActor.add(comment.getId());
                    actor.setComments(commentsActor);
                    actorRepository.save(actor);
                }

            }
        }

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create a response to a certain comment")
    @PostMapping(value = "/{idCommentToRespond}/response/{idAuthor}", produces = "application/json")
    public ResponseEntity<?> createResponse(@PathVariable("idAuthor") String idAuthor, @PathVariable("idCommentToRespond") String id, @RequestBody Comment comment, UriComponentsBuilder ucBuilder) {
        Comment commentToResponse = commentRepository.findOne(id);
        Actor actor = actorRepository.findOne(idAuthor);
        List<String> responses;
        List<String> commentsActor;

        comment.setCommentable(commentToResponse.getCommentable());

        commentRepository.save(comment);

        if (postRepository.findOne(comment.getCommentable()) != null){
            Post post = postRepository.findOne(comment.getCommentable());
            try {
                List<String> newComments = post.getComments();
                newComments.add(comment.getId());
                post.setComments(newComments);
                postRepository.save(post);
            } catch (NullPointerException e){
                List<String> newComments = new ArrayList<>();
                newComments.add(comment.getId());
                post.setComments(newComments);
                postRepository.save(post);
            }
        } else {
            Conference conference = conferenceRepository.findOne(comment.getCommentable());
            try {
                List<String> newComments = conference.getComments();
                newComments.add(comment.getId());
                conference.setComments(newComments);
                conferenceRepository.save(conference);
            } catch (NullPointerException e){
                List<String> newComments = new ArrayList<>();
                newComments.add(comment.getId());
                conference.setComments(newComments);
                conferenceRepository.save(conference);
            }
        }

        try {
            responses = commentToResponse.getResponses();
            responses.add(comment.getId());
            commentToResponse.setResponses(responses);
            commentRepository.save(commentToResponse);

            try {
                commentsActor = actor.getComments();
                commentsActor.add(comment.getId());
                actor.setComments(commentsActor);
                actorRepository.save(actor);
            } catch (NullPointerException e1){
                commentsActor = new ArrayList<>();
                commentsActor.add(comment.getId());
                actor.setComments(commentsActor);
                actorRepository.save(actor);
            }

        } catch (NullPointerException e){
            responses = new ArrayList<>();
            responses.add(comment.getId());
            commentToResponse.setResponses(responses);
            commentRepository.save(commentToResponse);

            try {
                commentsActor = actor.getComments();
                commentsActor.add(comment.getId());
                actor.setComments(commentsActor);
                actorRepository.save(actor);
            } catch (NullPointerException e1){
                commentsActor = new ArrayList<>();
                commentsActor.add(comment.getId());
                actor.setComments(commentsActor);
                actorRepository.save(actor);
            }
        }

        commentRepository.save(comment);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/comment/{idComment}").buildAndExpand(comment.getId()).toUri());

        return new ResponseEntity<>(comment, headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Edit a certain comment")
	@PutMapping(value = "/{idComment}", produces = "application/json")
	public ResponseEntity<?> edit(@PathVariable("idComment") String id, @RequestBody Comment comment) {
		Comment currentComment = commentRepository.findOne(id);

		if (currentComment == null) {
			return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
		}

		currentComment.setText(comment.getText());
		currentComment.setTitle(comment.getTitle());

		commentRepository.save(currentComment);
		return new ResponseEntity<>(currentComment, HttpStatus.OK);
	}

    @ApiOperation(value = "Delete a certain comment")
	@DeleteMapping(value = "/{idComment}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("idComment") String idComment, @PathVariable("commentableRol") String commentableRol) {
        Comment comment = commentRepository.findOne(idComment);
        List<Comment> responses = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();
        Actor actor = null;

        if (comment == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        // Get actor
        for (Actor a: actorRepository.findAll()){
            if(a.getComments().contains(comment.getId())){
                actor = a;
                break;
            }
        }

        // Delete the comment in the commentable element
        if (postRepository.findOne(idComment) != null){

            for (Post p : postRepository.findAll()){
                for (String p1 : p.getComments()){
                    if (p1.equals(idComment)){
                        List<String> commentsActor = p.getComments();
                        commentsActor.remove(p1);
                        postRepository.save(p);
                        break;
                    }
                }
            }
        } else {
            for (Conference c : conferenceRepository.findAll()){
                for (String c1 : c.getComments()){
                    if (c1.equals(idComment)){
                        List<String> commentsActor = c.getComments();
                        commentsActor.remove(c1);
                        conferenceRepository.save(c);
                        break;
                    }
                }
            }
        }

        // Delete comment in actor
        if (actor.getComments().contains(idComment)){
            List<String> commentsActor = actor.getComments();
            commentsActor.remove(idComment);
            actor.setComments(commentsActor);
            actorRepository.save(actor);
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

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        return dateTime;
    }
}
