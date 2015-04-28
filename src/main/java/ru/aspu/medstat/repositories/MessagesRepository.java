package ru.aspu.medstat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.aspu.medstat.entities.Message;

@Repository
public interface MessagesRepository extends CrudRepository<Message, Long> {
	@Query("SELECT m FROM Message m "
			+ "WHERE m.fromUser.id = :from_id "
			+ "AND m.deleteFrom = 0 "
			+ "ORDER BY m.date DESC")
	public List<Message> findAllByFromUser(@Param("from_id") long from_id);
	
	@Query("SELECT m FROM Message m "
			+ "WHERE m.toUser.id = :to_id "
			+ "AND m.deleteTo = 0 "
			+ "ORDER BY m.date DESC")
	public List<Message> findAllByToUser(@Param("to_id") long to_id);
	
	@Query("SELECT COUNT(m) FROM Message m "
			+ "WHERE m.toUser.id = :to_id "
			+ "AND m.isRead = 0 " 
			+ "AND m.deleteTo = 0")
	public int countUnreadUserMessages(@Param("to_id") long to_id);
}