package com.concamap.services.post;

import com.concamap.model.Post;
import com.concamap.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PostServiceImp implements PostService {
    public static final int START_INDEX = 1;
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAllByStatus(int status, Sort sort) {
        List<Post> postList = new LinkedList<>();
        Iterable<Post> iterable = postRepository.findAllByStatus(status, sort);
        for (Post post : iterable) {
            postList.add(post);
        }
        return postList;
    }

    @Override
    public Page<Post> findAllByStatus(int status, Pageable pageable) {
        return postRepository.findAllByStatus(status, pageable);
    }

    @Override
    public List<Post> findRandomByStatus(int status, int quantity) {
        int count = 1;
        Random random = new Random();
        List<Post> postList = new LinkedList<>();
        while (count <= quantity) {
            int id = random.nextInt((int) postRepository.count());
            postList.add(postRepository.findById(id).orElse(null));
            count++;
        }
        return postList;
    }

    @Override
    public Page<Post> findRecentPostByStatus(int status, int quantity) {
        Pageable pageable = PageRequest.of(START_INDEX, quantity);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        return postRepository.findAllByStatusAndCreatedDateBefore(status, currentTime, pageable);
    }

    @Override
    public Post findByIdAndStatus(int id, int status) {
        return postRepository.findByIdAndStatus(id, status).orElse(null);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public boolean delete(int id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setStatus(0);
            postRepository.save(post);
            return true;
        }
        return false;
    }
}
