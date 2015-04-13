package ru.aspu.medstat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.aspu.medstat.entities.Statistic;

import java.util.List;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistic, Long> {
	@Query("SELECT s FROM Statistic s, UserGym ug "
			+ "WHERE s.userGym.id = ug.id "
			+ "AND ug.user.id = :user_id "
			+ "AND ug.gymnastic.id = :gymnastic_id "
			+ "AND ug.complete = 0 "
			+ "ORDER BY s.date DESC")
    public List<Statistic> findAllActualUserStatsByGymnastic(@Param("user_id") long user_id, @Param("gymnastic_id") long gymnastic_id);

	@Query("SELECT s FROM Statistic s, UserGym ug "
			+ "WHERE s.userGym.id = ug.id "
			+ "AND ug.user.id = :user_id "
			+ "AND ug.complete = 0 "
			+ "ORDER BY s.date DESC")
    public List<Statistic> findAllActualUserStats(@Param("user_id") long user_id);
    
    @Query("SELECT s FROM Statistic s, UserGym ug "
    		+ "WHERE s.userGym.id = ug.id "
    		+ "AND ug.complete = 0 "
    		+ "ORDER BY s.date DESC")
    public List<Statistic> findAllActualStats();
    
    @Query("SELECT s FROM Statistic s, UserGym ug, User u "
    		+ "WHERE s.userGym.id = ug.id "
    		+ "AND ug.user.id = u.id "
    		+ "AND ug.complete = 0 "
    		+ "AND u.doctorId = :doctor_id "
    		+ "ORDER BY s.date DESC")
    public List<Statistic> findAllActualUsersStatsByDoctor(@Param("doctor_id") long doctor_id);
}
