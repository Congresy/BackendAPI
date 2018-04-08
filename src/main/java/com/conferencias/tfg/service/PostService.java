package com.conferencias.tfg.service;

import com.conferencias.tfg.domain.Post;
import com.conferencias.tfg.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post edit(Post actual, Post editado) {
        actual.setTitle(editado.getTitle());
        actual.setBody(editado.getBody());
        actual.setCategory(editado.getCategory());
        return postRepository.save(actual);

    }
}
