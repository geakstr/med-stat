package ru.aspu.medstat.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.repositories.StatisticsRepository;

import java.util.List;

@Component
public class StatisticsService {
    @Autowired
    private StatisticsRepository repo;

    public List<Statistic> getAllUserStats(final long userId) {
        return repo.findAllUserStats(userId);
    }

    public List<Statistic> getAllUserStatsByGymnastic(final long userId, final long gymnasticId) {
        return repo.findAllUserStatsByGymnastic(userId, gymnasticId);
    }
    
    public List<Statistic> getAllStats() {
    	return repo.findAllStats();
    }
    
    public List<Statistic> getAllUsersStatsByDoctor(final long doctorId) {
    	return repo.findAllUsersStatsByDoctor(doctorId);
    }
}
