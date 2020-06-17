package com.concamap.controllers;

import com.concamap.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class DemoController {
    private final PostService postService;

    @Autowired
    public DemoController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ModelAndView showIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("message", "Xin chào");
        modelAndView.addObject("posts", postService.findAll());
        System.out.println(postService.findAll());
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
