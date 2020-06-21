package com.concamap.controllers.user;

import com.concamap.model.Users;
import com.concamap.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ModelAndView showUser(@PathVariable("username") String username) {
        ModelAndView modelAndView = new ModelAndView("user/user-info");
        Users userFound = userService.findActiveUserByUsername(username);
        modelAndView.addObject("user", userFound);
        return modelAndView;
    }
}
