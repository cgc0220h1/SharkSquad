package com.concamap.controllers.home;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.security.UserDetailServiceImp;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    private final PostService postService;

    private final PostComponent postComponent;

    private final UserDetailServiceImp userDetailServiceImp;

    @Autowired
    public CategoryController(PostService postService, PostComponent postComponent, UserDetailServiceImp userDetailServiceImp) {
        this.postService = postService;
        this.postComponent = postComponent;
        this.userDetailServiceImp = userDetailServiceImp;
    }

    @GetMapping("/{anchor-name}/posts")
    public ModelAndView showPostOfCategories(@PathVariable("anchor-name") Category category,
                                             Pageable pageable,
                                             @SessionAttribute("recentPostList") List<Post> recentPosts,
                                             @SessionAttribute("randomPostList") List<Post> randomPosts,
                                             @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("post/filter");
        Page<Post> postPage = postService.findExistByCategory(category, pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        modelAndView.addObject("category", category);
        modelAndView.addObject("message", category.getTitle());
        modelAndView.addObject("postPage", postPage);
        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("randomPostList", randomPosts);
        modelAndView.addObject("categoryList", categoryList);

        modelAndView.addObject("user", userDetailServiceImp.getCurrentUser());
        return modelAndView;
    }
}
