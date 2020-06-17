package com.concamap.services;

import com.concamap.model.PostEntity;
import com.concamap.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostEntity> findAll() {
        List<PostEntity> posts = new LinkedList<>();
        for (PostEntity postEntity : postRepository.findAll()) {
            posts.add(postEntity);
        }
        return posts;
    }
}
