package com.concamap.services;

import com.concamap.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface GenericService<T> {
    List<T> findAllExist();

    List<T> findAllExist(Sort sort);

    Page<T> findAllExist(Pageable pageable);

    T findExistById(int id);

    List<T> findAllDeleted();

    List<T> findAllDeleted(Sort sort);

    Page<Post> findAllDeleted(Pageable pageable);

    T findDeletedById(int id);

    T save(T model);

    boolean delete(int id);
}