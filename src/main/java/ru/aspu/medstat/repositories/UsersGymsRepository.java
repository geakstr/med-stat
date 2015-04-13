package ru.aspu.medstat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.aspu.medstat.entities.UserGym;

@Repository
public interface UsersGymsRepository extends CrudRepository<UserGym, Long> { 
	@Query("SELECT ug FROM UserGym ug "
			+ "WHERE ug.user.id = :user_id "
			+ "AND ug.complete = 0")
	public List<UserGym> findActualUserGyms(@Param("user_id") long user_id);
	
	@Query("SELECT ug FROM UserGym ug "
			+ "WHERE ug.user.id = :user_id "
			+ "ORDER BY ug.complete")
	public List<UserGym> findAllUserGyms(@Param("user_id") long user_id);
}
