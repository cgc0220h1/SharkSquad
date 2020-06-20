package com.concamap.services.post;

import com.concamap.model.Post;
import com.concamap.services.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PostService extends GenericService<Post> {
    List<Post> findAllByStatus(int status, Sort sort);

    List<Post> findRandomByStatus(int status, int quantity);

    Page<Post> findRecentPostByStatus(int status, int quantity);
}
