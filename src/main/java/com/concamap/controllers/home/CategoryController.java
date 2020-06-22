package com.concamap.controllers.home;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    private final PostService postService;

    private final PostComponent postComponent;

    @Autowired
    public CategoryController(PostService postService, PostComponent postComponent) {
        this.postService = postService;
        this.postComponent = postComponent;
    }

    @GetMapping("/{anchor-name}/posts")
    public ModelAndView showPostOfCategories(@PathVariable("anchor-name") Category category, Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("post/viewByCategories");
        Page<Post> postPage = postService.findExistByCategory(category, pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        modelAndView.addObject("category", category);
        modelAndView.addObject("postPage", postPage);
        return modelAndView;
    }
}
