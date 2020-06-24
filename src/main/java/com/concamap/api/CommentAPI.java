package com.concamap.api;

import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.services.comment.CommentService;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@PropertySource("classpath:config/status.properties")
@RequestMapping("/api/comments")
public class CommentAPI {

    @Value("${entity.exist}")
    private int statusExist;

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public CommentAPI(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(value = "/{anchorName}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment createComment(@RequestBody Comment comment, @PathVariable String anchorName) {
        String username = comment.getUsers().getUsername();
        Users currentUser = userService.findActiveUserByUsername(username);
        Post currentPost = postService.findExistByAnchorName(anchorName);
        return commentService.save(comment, currentUser, currentPost);
    }

    @RequestMapping(value = "/{anchor-name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Comment> allComment(@PathVariable("anchor-name") String anchorName) {
        Post postFound = postService.findExistByAnchorName(anchorName);
        return commentService.findAllByPostAndStatus(postFound, statusExist);
    }
}