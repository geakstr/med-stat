package ru.aspu.medstat.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ru.aspu.medstat.entities.UserGym;

@Repository
public interface UsersGymsRepository extends CrudRepository<UserGym, Long> { }
