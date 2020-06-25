package com.concamap.controllers.home;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.security.UserDetailServiceImp;
import com.concamap.services.category.CategoryService;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.*;

@Controller
@RequestMapping("/")
@PropertySource("classpath:config/homepage.properties")
@SessionAttributes({"categoryList", "dateMap", "randomPostList", "recentPostList"})
public class HomeController {
    @Value("${homepage.random-post.quantity}")
    private int randomPosts;

    @Value("${homepage.recent-post.quantity}")
    private int recentPosts;

    @Value("${homepage.post.summary.extend}")
    private String extendString;

    @Value("${homepage.post.summary.words}")
    private int summaryWords;

    @Value("${homepage.recent-post.summary.words}")
    private int summaryRecentWords;

    private final PostService postService;

    private final CategoryService categoryService;

    private final PostComponent postComponent;

    private final UserDetailServiceImp userDetailServiceImp;

    @Autowired
    public HomeController(PostService postService, CategoryService categoryService, PostComponent postComponent, UserDetailServiceImp userDetailServiceImp) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.postComponent = postComponent;
        this.userDetailServiceImp = userDetailServiceImp;
    }

    @ModelAttribute("categoryList")
    public List<Category> categoryList() {
        return categoryService.findAllExist(Sort.by("title").ascending());
    }

    @ModelAttribute("dateMap")
    public Map<Year, Set<Month>> dateMap() {
        Map<Year, Set<Month>> dateMap = new LinkedHashMap<>();
        List<Post> postList = postService.findAllExist(Sort.by("createdDate").descending());
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
        List<Post> postList = postService.findExistRandom(randomPosts);
        for (Post post : postList) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        return postList;
    }

    @ModelAttribute("recentPostList")
    public List<Post> recentPosts() {
        List<Post> postList = postService.findExistRecent(recentPosts).getContent();
        for (Post post : postList) {
            post.setContent(postComponent.summary(post.getContent(), summaryRecentWords, ""));
        }
        return postList;
    }

    @GetMapping
    public ModelAndView showHomePage(Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("home/index");
        Page<Post> postPage = postService.findAllExist(pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, extendString));
        }
        modelAndView.addObject("postPage", postPage);
        modelAndView.addObject("user", userDetailServiceImp.getCurrentUser());
        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(@RequestParam("keyword") String keyword,
                               @PageableDefault(size = 4) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("home/result");
        Page<Post> postPage = postService.findExistByTitleOrContent(keyword, pageable);
        for (Post post : postPage) {
            post.setContent(postComponent.summary(post.getContent(), summaryWords, ""));
        }
        modelAndView.addObject("postPage", postPage);
        modelAndView.addObject("keyword", keyword);
        return modelAndView;
    }
}