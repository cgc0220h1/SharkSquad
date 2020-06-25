package com.concamap.controllers.home;

import com.concamap.model.Category;
import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.services.comment.CommentService;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@PropertySource("classpath:config/status.properties")
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/{anchor-name}")
    public ModelAndView showPost(@PathVariable("anchor-name") String anchorName,
                                 @SessionAttribute("recentPostList") List<Post> recentPosts,
                                 @SessionAttribute("randomPostList") List<Post> randomPosts,
                                 @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("post/detail");
        Post postFound = postService.findExistByAnchorName(anchorName);

        List<Comment> allComment = commentService.findAllExistByPost(postFound);

        modelAndView.addObject("post", postFound);
        modelAndView.addObject("allComment", allComment);

        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("randomPostList", randomPosts);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

//    @GetMapping("/user/{id}/create")
//    public ModelAndView showCreatePost (@PathVariable("id") )
//
//    @PostMapping("/user/{id}/create")
//    public ModelAndView create
}
