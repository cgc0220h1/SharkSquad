package com.concamap.services.comment;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.services.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService extends GenericService<Comment> {

    List<Comment> findAllExistByPost(Post post);

    Page<Comment> findAllExistByPost(Post post, Pageable pageable);

//    Comment findByIdAndStatus(int id, int status);

//    Comment save(Comment comment, Users user, Post post);

//    void delete(Comment comment, Post post);
}