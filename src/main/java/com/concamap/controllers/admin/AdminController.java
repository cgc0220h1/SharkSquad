package com.concamap.controllers.admin;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Roles;
import com.concamap.model.Users;
import com.concamap.services.category.CategoryService;
import com.concamap.services.post.PostService;
import com.concamap.services.role.RoleService;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;
import java.util.List;

@Controller
@PropertySource({"classpath:config/homepage.properties", "classpath:config/status.properties"})
@RequestMapping("/admin")
public class AdminController {

    @Value("1")
    private int activeStatus;

    @Value("2")
    private int nonActiveStatus;

    @Value("0")
    private int deletedStatus;

    private final PostService postService;

    private final CategoryService categoryService;

    private final UserService userService;

    private final RoleService roleService;


    @Autowired
    public AdminController(PostService postService, CategoryService categoryService, UserService userService, RoleService roleService) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/overview")
    public ModelAndView loadDashboard(Pageable pageable) throws Exception {
        ModelAndView dashboard = new ModelAndView("admin/dashboard");
        Page<Post> posts = postService.findAllExist(pageable);
        dashboard.addObject("allPost", posts);
        return dashboard;
    }

    @GetMapping("/categories")
    public ModelAndView loadCategories(Pageable pageable) {
        ModelAndView categoriesView = new ModelAndView("admin/categories");
        Page<Category> categories = categoryService.findAllExist(pageable);
        categoriesView.addObject("categories", categories);
        return categoriesView;
    }

    @GetMapping("/users")
    public ModelAndView loadUsers(Pageable pageable) {
        ModelAndView usersView = new ModelAndView("admin/users");
        Page<Users> users = userService.findAllExist(pageable);
        usersView.addObject("users", users);
        return usersView;
    }

    @GetMapping("/users/create")
    public ModelAndView shoCreateForm(ModelAndView modelAndView,Users createUser){
        modelAndView.setViewName("admin/createUser");
        modelAndView.addObject("createUser", createUser);
        List<Roles> rolesList = roleService.findAllExist();
        modelAndView.addObject("roleList", rolesList);
        return modelAndView;
    }

    @PostMapping("/users/create")
    public RedirectView saveUser(@ModelAttribute("createUser") Users createUser) {
        createUser.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        createUser.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        createUser.setStatus(activeStatus);
        userService.save(createUser);
        return new RedirectView("/admin/users");
    }


    @GetMapping("/users/{id}")
    public ModelAndView loadDetailUser(@PathVariable("id") Integer id) {
        ModelAndView detailUser = new ModelAndView("admin/detailUser");
        Users currentUser = userService.findById(id);
        detailUser.addObject("currentUser", currentUser);
        return detailUser;
    }

    @GetMapping("/users/edit/{id}")
    public ModelAndView showEdit(@PathVariable("id") Integer id, ModelAndView modelAndView) {
        modelAndView.setViewName("admin/editUser");
        Users editUsers = userService.findById(id);
        List<Roles> rolesList = roleService.findAllExist();
        modelAndView.addObject("roleList", rolesList);
        modelAndView.addObject("editUser", editUsers);
        return modelAndView;
    }

    @PostMapping("/users/edit/{id}")
    public RedirectView editUser(@ModelAttribute("editUser") Users editUsers) {
        int id = editUsers.getId();
        Users users = userService.findById(id);
        users.setFirstName(editUsers.getFirstName());
        users.setLastName(editUsers.getLastName());
        users.setPhone(editUsers.getPhone());
        users.setRoles(editUsers.getRoles());
        users.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
        userService.save(users);
        return new RedirectView("/admin/users");
    }

    @GetMapping("/users/delete/{id}")
    public RedirectView showDelete(@PathVariable("id") Integer id) {
        Users deleteUser = userService.findById(id);
        deleteUser.setStatus(deletedStatus);
        userService.save(deleteUser);
        return new RedirectView("/admin/users");
    }

//    @PostMapping("/users/delete/{id}")
//    public RedirectView deleteUser(@ModelAttribute("deleteUser") Users deleteUser) {
//        deleteUser.setStatus(deletedStatus);
//        return new RedirectView("/admin/users");
//    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFound(Exception exception) {
        System.err.println(exception.getMessage());
    }
}
