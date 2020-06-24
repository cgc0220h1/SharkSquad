package com.concamap.controllers.admin;

import com.concamap.model.Post;
import com.concamap.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    ModelAndView loadDashboard(Pageable pageable) {
        ModelAndView dashboard = null;
        Page<Post> posts;
        try {
            posts = postService.findAllExist(pageable);
            dashboard= new ModelAndView("admin/dashboard");
            dashboard.addObject("allPost", posts);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return dashboard;
    }
}
