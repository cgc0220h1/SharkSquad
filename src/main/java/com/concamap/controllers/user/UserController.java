package com.concamap.controllers.user;

import com.concamap.model.Users;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ModelAndView showLogin() {
        ModelAndView modelAndView = new ModelAndView("user/login");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

    @PostMapping("/login")
    public RedirectView login(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        Users users1 = (Users) userService.findAllExist();
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
    public ModelAndView showUser(@PathVariable("username") String username) {
        ModelAndView modelAndView = new ModelAndView("user/user-info");
        Users userFound = userService.findActiveUserByUsername(username);
        modelAndView.addObject("user", userFound);
        return modelAndView;
    }
}
