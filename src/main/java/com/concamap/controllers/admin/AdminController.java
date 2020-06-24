package com.concamap.controllers.admin;

import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PropertySource("classpath:config/homepage.properties")
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;

    @Autowired
    public AdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/overview")
    ModelAndView loadDashboard(Pageable pageable) throws Exception {
        ModelAndView dashboard = new ModelAndView("admin/dashboard");
        Page<Post> posts = postService.findAllExist(pageable);
        dashboard.addObject("allPost", posts);
        return dashboard;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(Exception exception) {
        System.err.println(exception.getMessage());
    }
}
