package ru.aspu.medstat.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.services.StatisticsService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsApi {
	private static final Logger log = Logger.getLogger(StatisticsApi.class);
	
	@Autowired
	private StatisticsService statService;
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<Statistic> getUserById(final @PathVariable Long userId) {
        return statService.getAllUserStats(userId);
    }
	
	@RequestMapping(value = "/user/{userId}/{gymnasticId}", method = RequestMethod.GET)
    public List<Statistic> getUserById(final @PathVariable Long userId, 
    								   final @PathVariable Long gymnasticId) {
        return statService.getAllUserStatsByGymnastic(userId, gymnasticId);
    }
}
