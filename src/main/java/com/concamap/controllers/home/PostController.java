package com.concamap.controllers.home;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Value("${homepage.random-post.quantity}")
    private int randomPosts;

    @Value("${homepage.recent-post.quantity}")
    private int recentPosts;

    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    private final PostService postService;

    private final PostComponent postComponent;

    @Autowired
    public PostController(PostService postService, PostComponent postComponent) {
        this.postService = postService;
        this.postComponent = postComponent;
    }

    @ModelAttribute("randomPostList")
    public List<Post> randomPosts() {
        List<Post> postList = postService.findExistRandom(randomPosts);
        for (Post post : postList) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        return postList;
    }

    @GetMapping("/{anchor-name}")
    public ModelAndView showPost(@PathVariable("anchor-name") String anchorName, @ModelAttribute("randomPostList") List<Post> randomPosts) {
        ModelAndView modelAndView = new ModelAndView("post/detail");
        Post postFound = postService.findExistByAnchorName(anchorName);
        modelAndView.addObject("post", postFound);
        modelAndView.addObject("randomPostList", randomPosts);
        return modelAndView;
    }

//    @GetMapping("/user/{id}/create")
//    public ModelAndView showCreatePost (@PathVariable("id") )
//
//    @PostMapping("/user/{id}/create")
//    public ModelAndView create
}
