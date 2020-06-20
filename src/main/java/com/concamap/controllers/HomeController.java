package com.concamap.controllers;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.services.category.CategoryService;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {
    private final static int STATUS_EXIST = 1;

    private final static int RANDOM_POSTS = 3;

    private final static int RECENT_POSTS = 5;

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

    @ModelAttribute("dateMap")
    public Map<Year, Set<Month>> dateMap() {
        Map<Year, Set<Month>> dateMap = new LinkedHashMap<>();
        List<Post> postList = postService.findAllByStatus(STATUS_EXIST, Sort.by("createdDate").descending());
        for (Post post : postList) {
            LocalDate postDate = post.getCreatedDate().toLocalDateTime().toLocalDate();
            Year postYear = Year.from(postDate);
            Month postMonth = Month.from(postDate);
            if (!dateMap.containsKey(postYear)) {
                Set<Month> monthSet = new LinkedHashSet<>();
                dateMap.put(postYear, monthSet);
            }
            dateMap.get(postYear).add(postMonth);
        }
        return dateMap;
    }

    @ModelAttribute("randomPostList")
    public List<Post> randomPosts() {
        return postService.findRandomByStatus(STATUS_EXIST, RANDOM_POSTS);
    }

    @ModelAttribute("recentPostList")
    public List<Post> recentPosts() {
        return postService.findRecentPostByStatus(STATUS_EXIST, RECENT_POSTS).getContent();
    }

    @GetMapping
    public ModelAndView showHomePage(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Post> postPage = postService.findAllByStatus(STATUS_EXIST, pageable);
        modelAndView.addObject("postPage", postPage);
        return modelAndView;
    }

}
