package com.concamap.repositories;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {

    List<Comment> findAllByPostAndStatus(Post post, int status);

    Comment findByIdAndStatus(int id, int status);
}