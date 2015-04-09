package ru.aspu.medstat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.aspu.medstat.entities.Gymnastic;

@Repository
public interface GymnasticRepository extends CrudRepository<Gymnastic, Long> {
	@Query("SELECT DISTINCT g FROM Gymnastic g, Statistic s WHERE s.user.id = :user_id")
    public List<Gymnastic> findAllUserGymnastics(@Param("user_id") long user_id);
}
