package ru.aspu.medstat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.repositories.StatisticsRepository;
import ru.aspu.medstat.repositories.UsersGymsRepository;

@Component
public class StatisticsService {
    @Autowired
    private StatisticsRepository repo;
    
    @Autowired
    private UsersGymsRepository ugRepo;

    public List<Statistic> getAllActualUserStats(final long userId) {
        return repo.findAllActualUserStats(userId);
    }

    public List<Statistic> getAllActualUserStatsByGymnastic(final long userId, final long gymnasticId) {
        return repo.findAllActualUserStatsByGymnastic(userId, gymnasticId);
    }
    
    public List<Statistic> getAllActualStats() {
    	return repo.findAllActualStats();
    }
    
    public List<Statistic> getAllActualUsersStatsByDoctor(final long doctorId) {
    	return repo.findAllActualUsersStatsByDoctor(doctorId);
    }
}
