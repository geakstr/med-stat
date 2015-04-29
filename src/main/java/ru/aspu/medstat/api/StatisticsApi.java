package ru.aspu.medstat.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.UserStatsResponse;
import ru.aspu.medstat.services.StatisticsService;
import ru.aspu.medstat.services.UsersService;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsApi {
    private static final Logger log = Logger.getLogger(StatisticsApi.class);

    @Autowired
    private StatisticsService statService;
    
    @Autowired
    private UserRepository usersRepo;
    
    @Autowired
    private UsersService usersService;

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public UserStatsResponse getUserById(final @PathVariable Long userId) {
        return new UserStatsResponse(usersService.userToJson(usersRepo.findOne(userId)));
    }

    @RequestMapping(value = "/user/{userId}/{gymnasticId}", method = RequestMethod.GET)
    public List<Statistic> getUserById(final @PathVariable Long userId,
                                       final @PathVariable Long gymnasticId) {
        return statService.getAllActualUserStatsByGymnastic(userId, gymnasticId);
    }
}
