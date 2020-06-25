package com.concamap.services.post;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.services.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;

public interface PostService extends GenericService<Post> {
    List<Post> findExistRandom(int quantity);

    Page<Post> findExistRecent(int quantity);

    Page<Post> findAllByUsers_Username(String username, Pageable pageable);

    Page<Post> findExistByCategory(Category category, Pageable pageable);

    Page<Post> findExistWithinTime(Timestamp startDate, Timestamp endDate, Pageable pageable);

    Page<Post> findExistByTitle(String title, Pageable pageable);

    Page<Post> findExistByContent(String content, Pageable pageable);

    Page<Post> findExistByTitleAndContent(String title, String content, Pageable pageable);

    Page<Post> findExistByTitleOrContent(String query, Pageable pageable);

    Post findExistByAnchorName(String anchorName);

    int count();
}
