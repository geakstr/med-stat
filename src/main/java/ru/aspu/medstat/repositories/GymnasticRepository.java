package ru.aspu.medstat.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.aspu.medstat.entities.Gymnastic;

@Repository
public interface GymnasticRepository extends CrudRepository<Gymnastic, Long> { }
