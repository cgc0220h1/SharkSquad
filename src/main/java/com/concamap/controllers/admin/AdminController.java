package com.concamap.controllers.admin;

import com.concamap.component.post.PostComponent;
import com.concamap.model.Category;
import com.concamap.model.Comment;
import com.concamap.model.Post;
import com.concamap.model.Roles;
import com.concamap.model.Users;
import com.concamap.services.category.CategoryService;
import com.concamap.services.comment.CommentService;
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
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

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

    private final CommentService commentService;

    private final RoleService roleService;

    private final PostComponent postComponent;

    @Autowired
    public AdminController(PostService postService, CategoryService categoryService, UserService userService, CommentService commentService, RoleService roleService, PostComponent postComponent) {
        this.postService = postService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.commentService = commentService;
        this.roleService = roleService;
        this.postComponent = postComponent;
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
//        http://localhost:8888/admin/categories/suc-khoe/edit
        long now = System.currentTimeMillis();
        categoryFound.setDescription(category.getDescription());
        categoryFound.setTitle(category.getTitle());
        categoryFound.setUpdatedDate(new Timestamp(now));
        categoryFound.setAnchorName(postComponent.toAnchorName(category.getDescription()));
        categoryService.save(categoryFound);
        return new RedirectView("/admin/categories");
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
        category.setStatus(1);
        category.setUpdatedDate(new Timestamp(now));
        category.setCreatedDate(new Timestamp(now));
        category.setAnchorName(postComponent.toAnchorName(category.getDescription()));
        categoryService.save(category);
        return new RedirectView("/admin/categories");
    }

    @GetMapping("/categories/{anchor-name}/delete")
    public RedirectView deleteCategory(@PathVariable("anchor-name") String anchorName) {
        Category category = categoryService.findByAnchorName(anchorName);
        category.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        category.setStatus(0);
        categoryService.save(category);
        return new RedirectView("/admin/categories");
    }

    @GetMapping("/users/posts/{anchor-name}/delete")
    public RedirectView adminDeletePost(@PathVariable("anchor-name") String anchorName) {
        Post post = postService.findExistByAnchorName(anchorName);
        post.setStatus(0);
        post.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        postService.save(post);
        return new RedirectView("/admin/overview");
    }

    @GetMapping("/users/{username}/posts/{anchor-name}")
    public ModelAndView adminViewPost(@PathVariable("anchor-name") String anchorName,
                                      @PathVariable("username") String username,
                                      @SessionAttribute("recentPostList") List<Post> recentPosts,
                                      @SessionAttribute("randomPostList") List<Post> randomPosts,
                                      @SessionAttribute("categoryList") List<Category> categoryList) {
        ModelAndView modelAndView = new ModelAndView("admin/viewDetailPost");
        Post postFound = postService.findExistByAnchorName(anchorName);

        List<Comment> allComment = commentService.findAllExistByPost(postFound);

        modelAndView.addObject("post", postFound);
        modelAndView.addObject("allComment", allComment);
        modelAndView.addObject("recentPostList", recentPosts);
        modelAndView.addObject("randomPostList", randomPosts);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @GetMapping("/users")
    public ModelAndView loadUsers(Pageable pageable) {
        ModelAndView usersView = new ModelAndView("admin/users");
        Page<Users> users = userService.findAllExist(pageable);
        usersView.addObject("users", users);
        return usersView;
    }

    @GetMapping("/users/create")
    public ModelAndView shoCreateForm(ModelAndView modelAndView, Users createUser) {
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
