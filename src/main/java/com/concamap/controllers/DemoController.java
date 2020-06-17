package com.concamap.controllers;

import com.concamap.model.DemoEntity;
import com.concamap.services.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class DemoController {
    private final DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping
    public ModelAndView showIndex() {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("message", "Xin chào");
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("Duc");
        System.out.println(demoEntity.getName());
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
