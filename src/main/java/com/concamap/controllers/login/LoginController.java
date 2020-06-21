package com.concamap.controllers.login;

import com.concamap.model.Users;
import org.dom4j.rule.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping("")
    public ModelAndView showLogin() {
        ModelAndView modelAndView = new ModelAndView("login/login");
        modelAndView.addObject("users", new Users());
        return modelAndView;
    }

//    @PostMapping("")
//    public ModelAndView checkValidate(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
//        if (bindingResult.hasFieldErrors()) {
//            ModelAndView modelAndView = new ModelAndView("login/login");
//            return modelAndView;
//        }
//        ModelAndView modelAndView = new ModelAndView("login/index");
//        return modelAndView;
//    }

    @PostMapping("validateUser")
    public RedirectView login(@Validated @ModelAttribute("users") Users users, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return new RedirectView("/login/login");
        }
        return new RedirectView("/");
    }
}
