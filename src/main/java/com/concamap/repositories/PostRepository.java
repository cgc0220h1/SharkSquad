package com.concamap.repositories;

import com.concamap.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    Iterable<Post> findAllByStatusIs(int status);

    Iterable<Post> findAllByStatusIs(int status, Sort sort);

    Page<Post> findAllByStatusIs(int status, Pageable pageable);
}
