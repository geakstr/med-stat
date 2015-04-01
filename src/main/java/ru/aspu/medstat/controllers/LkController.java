package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lk")
public class LkController {
    private static final Logger log = Logger.getLogger(LkController.class);

    @RequestMapping("/")
    public String index(Model model) {
        return "lk/index";
    }
}
