package com.concamap.repositories;

import com.concamap.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
    Iterable<Category> findAllByStatusIs(int status);

    Iterable<Category> findAllByStatusIs(int status, Sort sort);

    Page<Category> findAllByStatusIs(int status, Pageable pageable);
}
