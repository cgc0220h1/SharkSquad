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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.List;

@Controller
@PropertySource("classpath:config/status.properties")
@RequestMapping("/api/comments")
public class CommentAPI {

    private final CommentService commentService;

    private final PostService postService;

    private final UserService userService;

    @Autowired
    public CommentAPI(CommentService commentService, PostService postService, UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping(value = "/{anchor-name}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment createComment(@RequestBody Comment comment, @PathVariable("anchor-name") String anchorName) {
        String username = comment.getUsers().getUsername();
        Users currentUser = userService.findActiveUserByUsername(username);
        Post currentPost = postService.findExistByAnchorName(anchorName);
        comment.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        comment.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        comment.setUsers(currentUser);
        comment.setPost(currentPost);
        return commentService.save(comment);
    }

    @PutMapping(value = "/{anchor-name}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment editComment(@RequestBody Comment comment, @PathVariable("anchor-name") String anchorName) throws EntityNotFoundException {
        Comment currentComment = commentService.findExistById(comment.getId());
        if (currentComment == null) {
            throw new EntityNotFoundException();
        }
        String username = comment.getUsers().getUsername();
        Users currentUser = userService.findActiveUserByUsername(username);
        Post currentPost = postService.findExistByAnchorName(anchorName);
        currentComment.setUsers(currentUser);
        currentComment.setPost(currentPost);
        currentComment.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        return commentService.save(currentComment);
    }

    @DeleteMapping(value = "/{anchor-name}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void deleteComment(@RequestBody Comment comment, @PathVariable("anchor-name") String anchorName) throws EntityNotFoundException {
        Comment currentComment = commentService.findExistById(comment.getId());
        if (currentComment == null) {
            throw new EntityNotFoundException();
        }
        Post currentPost = postService.findExistByAnchorName(anchorName);
        if (currentComment.getPost() != currentPost) {
            throw new EntityNotFoundException();
        }
        commentService.delete(currentComment.getId());
    }

    @GetMapping(value = "/{anchor-name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Comment> allComment(@PathVariable("anchor-name") String anchorName) {
        Post postFound = postService.findExistByAnchorName(anchorName);
        return commentService.findAllExistByPost(postFound);
    }

    @GetMapping(value = "/{anchor-name}", params = "page", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Page<Comment> getComment(@PathVariable("anchor-name") String anchorName,
                                    @PageableDefault(sort = "createdDate", direction = Sort.Direction.ASC, size = 5)
                                            Pageable pageable) {
        Post postFound = postService.findExistByAnchorName(anchorName);
        return commentService.findAllExistByPost(postFound, pageable);
    }
}