package ru.aspu.medstat.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.aspu.medstat.entities.Statistic;

import java.util.List;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistic, Long> {
    @Query("SELECT s FROM Statistic s WHERE s.gymnastic.id = :gymnastic_id AND s.user.id = :user_id AND s.user.role = 1")
    public List<Statistic> findAllUserStatsByGymnastic(@Param("user_id") long user_id, @Param("gymnastic_id") long gymnastic_id);

    @Query("SELECT s FROM Statistic s WHERE s.user.id = :user_id AND s.user.role = 1 ORDER BY s.date DESC")
    public List<Statistic> findAllUserStats(@Param("user_id") long user_id);
}
