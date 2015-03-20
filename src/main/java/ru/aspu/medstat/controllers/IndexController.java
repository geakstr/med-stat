package ru.aspu.services.stats.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class IndexController {
    @RequestMapping("/")
    public String index() {
        return "index";
    }
}
