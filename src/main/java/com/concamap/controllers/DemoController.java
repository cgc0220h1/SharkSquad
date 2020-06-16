package com.concamap.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class DemoController {

    @GetMapping
    public ModelAndView showIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("message","Xin chào");
        return modelAndView;
    }

    @GetMapping("/form")
    public ModelAndView redirect(@RequestParam("input") String input) {
        System.out.println(input);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("result", input);
        return modelAndView;
    }
}
