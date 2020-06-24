package com.concamap.controllers.admin;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.services.category.CategoryService;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
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

    private final CategoryService categoryService;

    private final UserService userService;

    @Autowired
    public AdminController(PostService postService, CategoryService categoryService, UserService userService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/overview")
    ModelAndView loadDashboard(Pageable pageable) throws Exception {
        ModelAndView dashboard = new ModelAndView("admin/dashboard");
        Page<Post> posts = postService.findAllExist(pageable);
        dashboard.addObject("allPost", posts);
        return dashboard;
    }

    @GetMapping("/categories")
    ModelAndView loadCategories(Pageable pageable) {
        ModelAndView categoriesView = new ModelAndView("admin/categories");
        Page<Category> categories = categoryService.findAllExist(pageable);
        categoriesView.addObject("categories", categories);
        return categoriesView;
    }

    @GetMapping("/users")
    ModelAndView loadUsers(Pageable pageable) {
        ModelAndView usersView = new ModelAndView("admin/users");
        Page<Users> users = userService.findAllExist(pageable);
        usersView.addObject("users", users);
        return usersView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(Exception exception) {
        System.err.println(exception.getMessage());
    }
}
