package com.concamap.repositories;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {

    List<Comment> findAllByPostAndStatus(Post post, int status);

    Page<Comment> findAllByPostAndStatus(Post post, int statusExist, Pageable pageable);

    Comment findByIdAndStatus(int id, int status);
}