package com.concamap.services.comment;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

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
        return null;
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
        return false;
    }

    @Override
    public List<Comment> findAllByPostAndStatus(Post post, int status) {
        return commentRepository.findAllByPostAndStatus(post, status);
    }
}
