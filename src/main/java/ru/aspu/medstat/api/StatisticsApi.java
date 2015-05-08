package ru.aspu.medstat.api;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.aspu.medstat.entities.Statistic;
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
    public UserStatsResponse getUserById(final @PathVariable Long userId,
                                       final @PathVariable Long gymnasticId) {
    	
    	JSONObject user = usersService.userToJson(usersRepo.findOne(userId));
    	JSONArray stats = (JSONArray) user.get("stats");
    	
		Iterator<JSONObject> iterator = stats.iterator();
    	while (iterator.hasNext()) {
    		JSONObject stat = iterator.next();
    		if (!stat.containsKey(gymnasticId)) {
    			iterator.remove();
    		}
    	}
  
    	
    	
    	return new UserStatsResponse(user);
    }
}
