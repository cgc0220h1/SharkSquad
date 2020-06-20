package com.concamap.controllers;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.services.category.CategoryService;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final static int STATUS_EXIST = 1;

    private final PostService postService;

    private final CategoryService categoryService;

    @Autowired
    public HomeController(PostService postService, CategoryService categoryService) {
        this.postService = postService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categoryList")
    public List<Category> categoryList() {
        return categoryService.findAllByStatus(STATUS_EXIST);
    }

    @GetMapping
    public ModelAndView showHomePage(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Post> postPage = postService.findAllByStatus(STATUS_EXIST, pageable);
        modelAndView.addObject("postPage", postPage);
        return modelAndView;
    }

}
