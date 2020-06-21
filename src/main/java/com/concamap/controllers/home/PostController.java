package com.concamap.controllers.home;

import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{anchor-name}")
    public ModelAndView showPost(@PathVariable("anchor-name") String anchorName) {
        ModelAndView modelAndView = new ModelAndView("detail");
        Post postFound = postService.findExistByAnchorName(anchorName);
        modelAndView.addObject("post", postFound);
        return modelAndView;
    }
}
