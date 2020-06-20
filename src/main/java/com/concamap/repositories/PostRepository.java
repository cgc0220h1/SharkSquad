package com.concamap.repositories;

import com.concamap.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.sql.Timestamp;
import java.util.Optional;

public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
    Iterable<Post> findAllByStatus(int status);

    Iterable<Post> findAllByStatus(int status, Sort sort);

    Page<Post> findAllByStatus(int status, Pageable pageable);

    Optional<Post> findByIdAndStatus(int id, int status);

    Page<Post> findAllByStatusAndCreatedDateBefore(int status, Timestamp createdDate, Pageable pageable);
}
