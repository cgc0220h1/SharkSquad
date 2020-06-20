package com.concamap.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T> {
    Page<T> findAllByStatus(Integer status, Pageable pageable);

    T findById(Integer id);

    T save(T model);

    boolean delete(Integer id);
}