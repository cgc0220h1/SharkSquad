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
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

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

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D').replace(' ', '-');
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

    @GetMapping("/categories/{anchor-name}/edit")
    public ModelAndView showEditCateForm(@PathVariable("anchor-name") String anchorName) {
        Category category = categoryService.findByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("admin/categoriesEdit");
        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @PostMapping("/categories/edit")
    public RedirectView updateCategory(@ModelAttribute("category") Category category) {
        Category categoryFound = categoryService.findExistById(category.getId());
        long now = System.currentTimeMillis();
        categoryFound.setUpdatedDate(new Timestamp(now));
        categoryFound.setAnchorName(removeAccent(category.getDescription()));
        categoryService.save(categoryFound);
        return new RedirectView("/categories");
    }

    @GetMapping("/categories/create")
    public ModelAndView showCreateCateForm() {
        Category category = new Category();
        ModelAndView modelAndView = new ModelAndView("admin/categoriesCreate");
        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @PostMapping("/categories/create")
    public RedirectView createCategory(@ModelAttribute("category") Category category) {
        long now = System.currentTimeMillis();
        category.setUpdatedDate(new Timestamp(now));
        category.setCreatedDate(new Timestamp(now));
        category.setAnchorName(removeAccent(category.getDescription()));
        categoryService.save(category);
        return new RedirectView("/categories");
    }

    @GetMapping("/users")
    ModelAndView loadUsers(Pageable pageable) {
        ModelAndView usersView = new ModelAndView("admin/users");
        Page<Users> users = userService.findAllExist(pageable);
        usersView.addObject("users", users);
        return usersView;
    }

    @GetMapping("/user/{id}")
    ModelAndView loadDetailUser(@PathVariable("id") Integer id) {
        ModelAndView detailUser = new ModelAndView("admin/detailUser");
        Users currentUser = userService.findExistById(id);
        detailUser.addObject("currentUser", currentUser);
        return detailUser;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(Exception exception) {
        System.err.println(exception.getMessage());
    }
}
