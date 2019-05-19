package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Actor;
import com.conferencias.tfg.domain.Comment;
import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Post;
import com.conferencias.tfg.repository.ActorRepository;
import com.conferencias.tfg.repository.CommentRepository;
import com.conferencias.tfg.repository.PostRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

@CrossOrigin
@RestController
@RequestMapping("posts")
@Api(value = "Posts", description = "Operations related with posts")
public class Posts {

    private final PostRepository postRepository;

    private final ActorRepository actorRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public Posts(PostRepository postRepository, ActorRepository actorRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.actorRepository = actorRepository;
        this.commentRepository = commentRepository;
    }

    @ApiOperation(value = "List all system's posts", response = Iterable.class)
    @GetMapping()
    public ResponseEntity<?> showAll() {
        List<Post> res = new ArrayList<>(postRepository.findAll());

        res.sort(Comparator.comparingInt((Post c) -> parseDate(c.getPosted()).getNano()));

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "List own posts", response = Iterable.class)
    @GetMapping("/own/{idActor}")
    public ResponseEntity<?> showOwn(@PathVariable("idActor") String idActor) {
        Actor actor = actorRepository.findOne(idActor);

        List<Post> res = new ArrayList<>();

        try{
            for(String s : actor.getPosts()){
                if (postRepository.findOne(s) != null)
                    res.add(postRepository.findOne(s));
            }
            res.sort(Comparator.comparing((Post c) -> parseDate(c.getPosted())));
        } catch (Exception e){
            res = new ArrayList<>();
        }

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @ApiOperation(value = "Create a post")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Post post, UriComponentsBuilder ucBuilder) {

        Actor actor = actorRepository.findOne(post.getAuthorId());

        post.setAuthorName("** Not published yet **");

        postRepository.save(post);

        try {
            List<String> add = actor.getPosts();
            add.add(post.getId());
            actor.setPosts(add);
            actorRepository.save(actor);
        } catch (Exception e) {
            List<String> add = new ArrayList<>();
            add.add(post.getId());
            actor.setPosts(add);
            actorRepository.save(actor);
        }

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Make a post public")
    @PutMapping("/public/{idPost}")
    public ResponseEntity<?> makePublic(@PathVariable("idPost") String idPost, UriComponentsBuilder ucBuilder) {

        Post post = postRepository.findOne(idPost);

        Actor actor = actorRepository.findOne(post.getAuthorId());

        post.setAuthorName(actor.getName() + " " + actor.getSurname());
        post.setViews(0);
        post.setVotes(0);

        post.setDraft(false);

        postRepository.save(post);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a post by ID", response = Post.class)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        Post post = postRepository.findOne(id);

        if (post == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        post.setViews(post.getViews() + 1);

        postRepository.save(post);

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @ApiOperation(value = "Update a post by ID")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Post post) {
        Post currentpost = postRepository.findOne(id);

        if (currentpost == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        currentpost.setTitle(post.getTitle());
        currentpost.setBody(post.getBody());
        currentpost.setCategory(post.getCategory());
        currentpost.setVotes(currentpost.getVotes());
        currentpost.setViews(currentpost.getViews());
        currentpost.setAuthorName(currentpost.getAuthorName());
        currentpost.setComments(currentpost.getComments());
        currentpost.setDraft(currentpost.getDraft());
        currentpost.setAuthorId(currentpost.getAuthorId());
        currentpost.setPosted(currentpost.getPosted());

        postRepository.save(currentpost);

        return new ResponseEntity<>(currentpost, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a post by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Post post = postRepository.findOne(id);
        if (post == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        Actor actor = null;

        for (Actor a : actorRepository.findAll()){
            if(a.getPosts() != null){
                if(a.getPosts().contains(id)){
                    actor = a;
                    break;
                }
            }
        }

        if (post.getComments() != null) {
            // Delete comments related to conference

            // Delete the comment in the commentable element
            if (post.getComments() != null){
                for (String idComment : post.getComments()){
                    Comment comment = commentRepository.findOne(idComment);

                    // Delete form actors
                    for (Actor a : actorRepository.findAll()){

                        if (a.getComments() != null){
                            // Delete comment in actor
                            if (a.getComments().contains(idComment)){
                                List<String> commentsActor = a.getComments();
                                commentsActor.remove(idComment);
                                a.setComments(commentsActor);
                                actorRepository.save(a);
                            }
                        }
                    }

                    // Delete responses of comment
                    if (comment.getResponses() != null){
                        if (comment.getResponses().isEmpty()){
                            commentRepository.delete(idComment);
                        } else {
                            for (String s : comment.getResponses()){
                                if (s != null)
                                    commentRepository.delete(commentRepository.findOne(s));
                            }
                            commentRepository.delete(idComment);
                        }
                    } else {
                        commentRepository.delete(idComment);
                    }
                }
            }
        }

        for (Actor a : actorRepository.findAll()){
            if (a.getPosts() != null){
                if (a.getPosts().contains(post.getId())){
                    List<String> add = actor.getPosts();
                    add.remove(id);
                    actor.setPosts(add);
                    actorRepository.save(actor);
                }
            }
        }


        postRepository.delete(id);
        return new ResponseEntity<Conference>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get a post searching by keyword", response = Post.class)
    @GetMapping(value = "/search/{keyword}")
    public ResponseEntity<?> getByKeyword(@PathVariable("keyword") String keyword) {
        List<Post> postAux = postRepository.findAll();
        List<Post> posts = new ArrayList<>();

        for (Post p : postAux) {
            if (p.getBody().toLowerCase().contains(keyword.toLowerCase()))
                posts.add(p);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a post searching by category", response = Post.class)
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getAllByCategory(@PathVariable("category") String category) {
        List<Post> postsAux = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        for (Post p : postsAux) {
            if (p.getCategory().toLowerCase().equals(category.toLowerCase()))
                posts.add(p);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a post searching by title", response = Post.class)
    @GetMapping("/title/{title}")
    public ResponseEntity<?> getAllByTitle(@PathVariable("title") String title) {
        List<Post> postsAux = postRepository.findAll();
        List<Post> posts = new ArrayList<>();
        for (Post p : postsAux) {
            if (p.getCategory().toLowerCase().equals(title.toLowerCase()))
                posts.add(p);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @ApiOperation(value = "Get most voted posts", response = Iterable.class)
    @GetMapping("/votes")
    public ResponseEntity<?> getMostVoted() {
        List<Post> posts = postRepository.findAll();

        posts.sort(Comparator.comparing(Post::getVotes).reversed());

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @ApiOperation(value = "Vote a post")
    @PutMapping(value = "/votes/{idPost}", produces = "application/json", params = "action")
    public ResponseEntity<?> vote(@PathVariable("idPost") String idPost, @RequestParam("action") String action) {
        Post post = postRepository.findOne(idPost);

        if (post == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        if (action.equals("add")){
            post.setVotes(post.getVotes() + 1);
            postRepository.save(post);
        } else if (action.equals("delete")) {
            post.setVotes(post.getVotes() - 1);
            postRepository.save(post);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);
    }


    private Boolean postExist(Post post) {
        Boolean res = false;

        for (Post p : postRepository.findAll()) {
            if (post.equals(p)) {
                res = true;
                break;
            }
        }
        return res;
    }

    private LocalDateTime parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return LocalDateTime.parse(date, formatter);
    }
}
