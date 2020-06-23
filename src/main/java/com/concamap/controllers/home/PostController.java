package com.concamap.controllers.home;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{anchor-name}")
    public ModelAndView showPost(@PathVariable("anchor-name") String anchorName,
                                 @SessionAttribute("recentPostList") List<Post> recentPosts,
                                 @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("post/detail");
        Post postFound = postService.findExistByAnchorName(anchorName);
        modelAndView.addObject("post", postFound);
        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

}