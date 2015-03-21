package ru.aspu.medstat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import ru.aspu.medstat.entities.Customer;
import ru.aspu.medstat.repositories.CustomerRepository;

@Controller
public class IndexController {
    @Autowired
    private CustomerRepository repository;

    @RequestMapping("/")
    public String index(Model model) {
        repository.save(new Customer("Jack", "Bauer"));

        model.addAttribute("customers", repository.findAll());

        return "index";
    }
}
