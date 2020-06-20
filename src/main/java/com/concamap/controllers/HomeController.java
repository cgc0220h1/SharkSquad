package com.concamap.controllers;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.services.category.CategoryService;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource({"classpath:config/status.properties", "classpath:config/homepage.properties"})
public class HomeController {
    @Value("${entity.exist}")
    private int statusExist;

    @Value("${homepage.random-post.quantity}")
    private int randomPosts;

    @Value("${homepage.recent-post.quantity}")
    private int recentPosts;

    private final PostService postService;

    private final CategoryService categoryService;

    @Autowired
    public HomeController(PostService postService, CategoryService categoryService) {
        this.postService = postService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categoryList")
    public List<Category> categoryList() {
        return categoryService.findAllByStatus(statusExist);
    }

    @ModelAttribute("dateMap")
    public Map<Year, Set<Month>> dateMap() {
        Map<Year, Set<Month>> dateMap = new LinkedHashMap<>();
        List<Post> postList = postService.findAllByStatus(statusExist, Sort.by("createdDate").descending());
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
        return postService.findRandomByStatus(statusExist, randomPosts);
    }

    @ModelAttribute("recentPostList")
    public List<Post> recentPosts() {
        return postService.findRecentPostByStatus(statusExist, recentPosts).getContent();
    }

    @GetMapping
    public ModelAndView showHomePage(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("index");
        Page<Post> postPage = postService.findAllByStatus(statusExist, pageable);
        modelAndView.addObject("postPage", postPage);
        return modelAndView;
    }
}
