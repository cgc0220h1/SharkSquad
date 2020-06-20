package com.concamap.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;

public interface GenericService<T> {
    Page<T> findAllByStatus(int status, Pageable pageable);

    T findByIdAndStatus(int id, int status);

    T save(T model);

    boolean delete(int id);
}