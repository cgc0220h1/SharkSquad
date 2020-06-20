package com.concamap.services.post;

import com.concamap.model.Post;
import com.concamap.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findAllByStatus(Integer status, Pageable pageable) {
        return postRepository.findAllByStatusIs(status, pageable);
    }

    @Override
    public Post findById(Long id) throws EntityNotFoundException {
        return postRepository.findById(id).orElse(null);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public boolean delete(Long id) throws EntityNotFoundException {
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
