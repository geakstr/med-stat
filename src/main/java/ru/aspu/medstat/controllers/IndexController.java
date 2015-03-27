package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.aspu.medstat.forms.UserRegistrationForm;

@Controller
public class IndexController {
    private static final Logger log = Logger.getLogger(IndexController.class);

    @RequestMapping("/")
    public String index(Model model) {
    	model.addAttribute("title", "Главная страница");
    	
    	UserRegistrationForm form =  new UserRegistrationForm();
    	form.setAction("/users");
    	form.setMethod("post");
        model.addAttribute("regform", form);

        return "index/index";
    }
}
