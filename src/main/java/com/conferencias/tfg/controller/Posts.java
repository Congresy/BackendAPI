package com.conferencias.tfg.controller;

import com.conferencias.tfg.domain.Conference;
import com.conferencias.tfg.domain.Post;
import com.conferencias.tfg.repository.PostRepository;
import com.conferencias.tfg.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("posts")
@Api(value = "Posts", description = "Operations related with posts")
public class Posts {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @ApiOperation(value = "List all system's posts", response = Iterable.class)
    @GetMapping()
    public List<Post> showAll() {

        return postRepository.findAll();
    }

    @ApiOperation(value = "Create a post")
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> create(@RequestBody Post post, UriComponentsBuilder ucBuilder) {

        if (this.postExist(post)) {
            return new ResponseEntity<Error>(HttpStatus.CONFLICT);
        }
        Post aux = new Post();
        post.setId(aux.getId());
        postRepository.save(post);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/post/{id}").buildAndExpand(post.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get a post by ID", response = Post.class)
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id) {
        Post post = postRepository.findOne(id);

        if (post == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @ApiOperation(value = "Update a post by ID")
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> edit(@PathVariable("id") String id, @RequestBody Post post) {
        Post currentpost = postRepository.findOne(id);

        if (currentpost == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
        }
        postService.edit(currentpost, post);
        return new ResponseEntity<>(currentpost, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a post by ID")
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        Post post = postRepository.findOne(id);
        if (post == null) {
            return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
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
}
