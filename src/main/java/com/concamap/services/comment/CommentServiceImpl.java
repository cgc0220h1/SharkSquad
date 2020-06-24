package com.concamap.services.comment;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@PropertySource("classpath:config/status.properties")
public class CommentServiceImpl implements CommentService {

    @Value("${entity.exist}")
    private int statusExist;

    @Value("${entity.deleted}")
    private int statusDelete;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findAllExist() {
        return null;
    }

    @Override
    public List<Comment> findAllExist(Sort sort) {
        return null;
    }

    @Override
    public Page<Comment> findAllExist(Pageable pageable) {
        return null;
    }

    @Override
    public Comment findExistById(int id) {
        return commentRepository.findByIdAndStatus(id, statusExist);
    }

    @Override
    public List<Comment> findAllDeleted() {
        return null;
    }

    @Override
    public List<Comment> findAllDeleted(Sort sort) {
        return null;
    }

    @Override
    public Page<Post> findAllDeleted(Pageable pageable) {
        return null;
    }

    @Override
    public Comment findDeletedById(int id) {
        return null;
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean delete(int id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setStatus(statusDelete);
            commentRepository.save(comment);
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> findAllExistByPost(Post post) {
        return commentRepository.findAllByPostAndStatus(post, statusExist);
    }
}
