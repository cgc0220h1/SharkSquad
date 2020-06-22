package com.concamap.services.post;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@PropertySource("classpath:config/status.properties")
public class PostServiceImp implements PostService {
    public static final int START_INDEX = 1;

    @Value("${entity.exist}")
    private int statusExist;

    @Value("${entity.deleted}")
    private int statusDeleted;

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImp(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<Post> findExistRandom(int quantity) {
        int count = 1;
        long postSize = postRepository.count();
        Random random = new Random();
        List<Integer> randomId = new ArrayList<>();
        List<Post> postList = new LinkedList<>();
        while (count <= quantity) {
            if (count > postSize) {
                return postList;
            }
            int id = random.nextInt((int) postSize) + 1;
            if (!randomId.contains(id)) {
                postList.add(postRepository.findById(id).orElse(null));
                randomId.add(id);
                count++;
            }
        }
        return postList;
    }

    @Override
    public Page<Post> findExistRecent(int quantity) {
        Pageable pageable = PageRequest.of(START_INDEX, quantity);
        Timestamp currentTime = Timestamp.valueOf(LocalDateTime.now());
        return postRepository.findAllByStatusAndCreatedDateBefore(statusExist, currentTime, pageable);
    }

    @Override
    public Page<Post> findExistByCategory(Category category, Pageable pageable) {
        return postRepository.findByStatusAndCategory(statusExist, category, pageable);
    }

    @Override
    public Page<Post> findExistWithinTime(Timestamp startDate, Timestamp endDate, Pageable pageable) {
        return postRepository.findAllByStatusAndCreatedDateBetween(statusExist, startDate, endDate, pageable);
    }

    @Override
    public Page<Post> findExistByTitle(String title, Pageable pageable) {
        return postRepository.findAllByStatusAndTitleContains(statusExist, title, pageable);
    }

    @Override
    public Page<Post> findExistByContent(String content, Pageable pageable) {
        return postRepository.findAllByStatusAndContentContains(statusExist, content, pageable);
    }

    @Override
    public Page<Post> findExistByTitleAndContent(String title, String content, Pageable pageable) {
        return postRepository.findAllByStatusAndTitleContainsAndContentContains(
                statusExist, title, content, pageable);
    }

    @Override
    public Page<Post> findExistByTitleOrContent(String query, Pageable pageable) {
        List<Post> postsFoundByTitle = postRepository.findAllByStatusAndTitleContains(statusExist, query);
        List<Post> postsFoundByContent = postRepository.findAllByStatusAndContentContains(statusExist, query);
        for (Post post : postsFoundByTitle) {
            if (!postsFoundByContent.contains(post)) {
                postsFoundByContent.add(post);
            }
        }
        return new PageImpl<>(postsFoundByContent, pageable, postsFoundByContent.size());
    }

    @Override
    public Post findExistByAnchorName(String anchorName) {
        return postRepository.findByStatusAndAnchorName(statusExist, anchorName).orElse(null);
    }

    @Override
    public List<Post> findAllExist() {
        List<Post> postList = new LinkedList<>();
        Iterable<Post> iterable = postRepository.findAllByStatus(statusExist);
        for (Post post : iterable) {
            postList.add(post);
        }
        return postList;
    }

    @Override
    public List<Post> findAllExist(Sort sort) {
        List<Post> postList = new LinkedList<>();
        Iterable<Post> iterable = postRepository.findAllByStatus(statusExist, sort);
        for (Post post : iterable) {
            postList.add(post);
        }
        return postList;
    }

    @Override
    public Page<Post> findAllExist(Pageable pageable) {
        return postRepository.findAllByStatus(statusExist, pageable);
    }

    @Override
    public Post findExistById(int id) {
        return postRepository.findByStatusAndId(statusExist, id).orElse(null);
    }

    @Override
    public List<Post> findAllDeleted() {
        return null;
    }

    @Override
    public List<Post> findAllDeleted(Sort sort) {
        return null;
    }

    @Override
    public Page<Post> findAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public Post findDeletedById(int id) {
        return null;
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
            post.setStatus(statusDeleted);
            postRepository.save(post);
            return true;
        }
        return false;
    }
}
