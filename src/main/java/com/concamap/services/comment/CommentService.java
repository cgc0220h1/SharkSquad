package com.concamap.services.comment;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.services.GenericService;

import java.util.List;

public interface CommentService extends GenericService<Comment> {

    List<Comment> findAllByPostAndStatus(Post post, int status);
}
