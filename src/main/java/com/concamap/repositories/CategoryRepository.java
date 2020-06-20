package com.concamap.repositories;

import com.concamap.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
    Iterable<Category> findAllByStatusIs(Integer status);

    Iterable<Category> findAllByStatusIs(Integer status, Sort sort);

    Page<Category> findAllByStatusIs(Integer status, Pageable pageable);
}
