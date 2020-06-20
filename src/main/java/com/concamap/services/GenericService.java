package com.concamap.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T> {
    Page<T> findAllByStatus(Integer status, Pageable pageable);

    T findById(Long id);

    T save(T model);

    boolean delete(Long id);
}