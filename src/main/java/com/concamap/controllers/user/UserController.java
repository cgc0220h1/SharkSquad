package com.concamap.controllers.user;

import com.concamap.model.Category;
import com.concamap.model.Post;
import com.concamap.model.Users;
import com.concamap.services.post.PostService;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

    @PostMapping("/login")
    public RedirectView login(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new RedirectView("/login");
        }
        return new RedirectView("/");
    }

    @GetMapping("/signup")
    public ModelAndView showSignUp() {
        ModelAndView modelAndView = new ModelAndView("user/signup");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

    @PostMapping("/signup")
    public RedirectView signup(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new RedirectView("/signup");
        }
        return new RedirectView("/login");
    }

    @GetMapping("/users/{username}")
    public ModelAndView showUser(@PathVariable("username") Users user) {
        ModelAndView modelAndView = new ModelAndView("home/bio");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @GetMapping("/users/{username}/profile")
    public ModelAndView showUserProfile(@PathVariable("username") Users user) {
        ModelAndView modelAndView = new ModelAndView("user/profile");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/users/**/profile")
    public RedirectView updateUserProfile(@ModelAttribute("user") Users users) {
        Users usersFound = userService.findExistById(users.getId());
        usersFound.setFirstName(users.getFirstName());
        usersFound.setLastName(users.getLastName());
        usersFound.setPhone(users.getPhone());
        usersFound.setEmail(users.getEmail());
        usersFound.setBio(users.getBio());
        userService.save(usersFound);
        return new RedirectView("/users/" + usersFound.getUsername() + "/profile");
    }

    @GetMapping("/users/{id}/posts/create")
    public ModelAndView showCreateForm(@PathVariable("id") int id, @SessionAttribute("categoryList") List<Category> categoryList){
        Post post = new Post();
        ModelAndView modelAndView = new ModelAndView("post/create");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

//    @PostMapping("/users/{id}/create")
//    public ModelAndView savePost(@ModelAttribute("post")Post post, @PathVariable("id") int id){
//        ModelAndView modelAndView = new ModelAndView("post/create");
//        MultipartFile multipartFile = ;
//        String title, content, fileName, fileUpload;
//        File file;
//
//    }

    @GetMapping("/users/{id}/posts/{anchor-name}/edit")
    public ModelAndView showEditForm(@PathVariable("id") int id, @PathVariable("anchor-name") String anchorName, @SessionAttribute("categoryList") List<Category> categoryList){
        Post post = postService.findExistByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("post/edit");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @PostMapping("/users/{id}/posts/edit")
    public ModelAndView updatePost(@PathVariable("id") int id,@ModelAttribute("post")Post post, @SessionAttribute("categoryList") List<Category> categoryList){
        postService.save(post);
        ModelAndView modelAndView = new ModelAndView("post/edit");
        modelAndView.addObject("post", post);
        modelAndView.addObject("categoryList", categoryList);
        return modelAndView;
    }

    @GetMapping("/users/{id}/posts/{anchor-name}/delete")
    public ModelAndView showDeleteForm(@PathVariable("id") int id, @PathVariable("anchor-name") String anchorName){
        Post post = postService.findExistByAnchorName(anchorName);
        ModelAndView modelAndView = new ModelAndView("post/delete");
        modelAndView.addObject("post", post);
        return modelAndView;
    }

    @PostMapping("/users/{id}/posts/delete")
    public RedirectView deleteBook(@ModelAttribute("post")Post post){
        postService.delete(post.getId());
        return new RedirectView("/");
    }
}