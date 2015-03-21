package ru.aspu.medstat.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.aspu.medstat.entities.Customer;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}