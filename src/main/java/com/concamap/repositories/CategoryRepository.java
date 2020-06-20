package com.concamap.repositories;

import com.concamap.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    Iterable<Category> findAllByStatus(int status);

    Iterable<Category> findAllByStatus(int status, Sort sort);

    Page<Category> findAllByStatus(int status, Pageable pageable);

    Optional<Category> findByStatusAndAnchorName(int status, String anchorName);

    Optional<Category> findByStatusAndId(int status, int id);
}
