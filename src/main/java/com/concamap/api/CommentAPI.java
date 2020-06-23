package com.concamap.api;

import com.concamap.model.Comment;
import com.concamap.services.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/comments")
public class CommentAPI {

    private final CommentService commentService;

    @Autowired
    public CommentAPI(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = "/{}/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }

}
