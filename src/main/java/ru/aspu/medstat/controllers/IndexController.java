package ru.aspu.medstat.controllers;

import ru.aspu.medstat.repositories.*;
import ru.aspu.medstat.entities.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class IndexController {
    @Autowired
    private CustomerRepository repository;

    @RequestMapping("/")
    public String index() {
        repository.save(new Customer("Jack", "Bauer"));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : repository.findAll()) {
            System.out.println(customer);
        }
        System.out.println();

        return "index";
    }
}
