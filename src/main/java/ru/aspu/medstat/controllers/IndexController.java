package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    private static final Logger log = Logger.getLogger(IndexController.class);

    @RequestMapping("/")
    public String index(Model model) {
        return "index/index";
    }
}
