package com.concamap.repositories;

import com.concamap.model.PostEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<PostEntity, Long> {
}
