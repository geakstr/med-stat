package ru.aspu.medstat.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ru.aspu.medstat.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    public User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email) AND u.password = :password")
    public User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    
    @Query("SELECT u FROM User u WHERE u.emailToken = :emailToken")
    public User findByEmailToken(@Param("emailToken") String emailToken);
    
    @Query("SELECT u FROM User u WHERE u.doctorId = -1 AND u.role = 1 ORDER BY u.registrationDate")
    public List<User> findAllNewUsers();
    
    @Query("SELECT u FROM User u WHERE u.role = 2")
    public List<User> findAllDoctors();
}
