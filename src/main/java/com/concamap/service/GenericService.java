package com.concamap.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T> {
    public Page<T> findAllByStatus(Integer status, Pageable pageable);

    public T findById(Integer id);

    public T save(T model);

    public boolean delete(Integer id);
}