package ru.aspu.medstat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.aspu.medstat.entities.Gymnastic;

import java.util.List;

@Repository
public interface GymnasticRepository extends CrudRepository<Gymnastic, Long> {
	@Query("SELECT g FROM Gymnastic g, UserGym ug "
			+ "WHERE ug.gymnastic.id = g.id "
			+ "AND ug.user.id = :user_id "
			+ "AND ug.complete = 0")
	public List<Gymnastic> findAllUserGyms(@Param("user_id") long user_id);
}
