package ru.aspu.medstat.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.aspu.medstat.entities.Gymnastic;
import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.entities.UserRoles;
import ru.aspu.medstat.repositories.GymnasticRepository;
import ru.aspu.medstat.repositories.StatisticsRepository;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.services.StatisticsService;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {
	private static final Logger log = Logger.getLogger(StatisticsController.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StatisticsRepository statRepo;
	
	@Autowired
	private GymnasticRepository gymRepo;
	
	@Autowired
	private StatisticsService statService;
	
	@RequestMapping("/")
	public String index(Model model) {
        User u1 = new User();
    	User u2 = new User();
    	User u3 = new User();
    	User u4 = new User();
    	User u5 = new User();
    	
    	u1.lastName = "Pacient #1";
    	u1.role = UserRoles.PATIENT;
    	
    	u2.lastName = "Pacient #2";
    	u2.role = UserRoles.PATIENT;
    	
    	u3.lastName = "Pacient #3";
    	u3.role = UserRoles.PATIENT;
    	
    	u4.lastName = "Doctor #1";
    	u4.role = UserRoles.DOCTOR;
    	
    	u5.lastName = "Doctor #2";
    	u5.role = UserRoles.DOCTOR;
    	
    	userRepo.save(u1);
    	userRepo.save(u2);
    	userRepo.save(u3);
    	userRepo.save(u4);
    	userRepo.save(u5);
    	
    	Gymnastic g1 = new Gymnastic();
    	Gymnastic g2 = new Gymnastic();
    	Gymnastic g3 = new Gymnastic();
    	
    	g1.title = "Gymnastic #1";
    	g2.title = "Gymnastic #2";
    	g3.title = "Gymnastic #3";
    	
    	gymRepo.save(g1);
    	gymRepo.save(g2);
    	gymRepo.save(g3);
    	
    	Statistic s1 = new Statistic();
    	Statistic s2 = new Statistic();
    	Statistic s3 = new Statistic();
    	Statistic s4 = new Statistic();
    	Statistic s5 = new Statistic();
    	Statistic s6 = new Statistic();
    	Statistic s7 = new Statistic();
    	Statistic s8 = new Statistic();
    	Statistic s9 = new Statistic();
    	
    	s1.percent = 100.0;
    	s1.setGymnastic(g1);
    	
    	s2.percent = 100.0;
    	s2.setGymnastic(g2);
    	
    	s3.percent = 100.0;
    	s3.setGymnastic(g3);
    	
    	s4.percent = 100.0;
    	s4.setGymnastic(g1);
    	
    	s5.percent = 50.0;
    	s5.setGymnastic(g1);
    	
    	s6.percent = 50.0;
    	s6.setGymnastic(g2);
    	
    	s7.percent = 10.0;
    	s7.setGymnastic(g1);
    	
    	s8.percent = 20.0;
    	s8.setGymnastic(g1);
    	
    	s9.percent = 30.0;
    	s9.setGymnastic(g3);
    	
    	u1.addStatistic(s1);
    	u1.addStatistic(s2);
    	u1.addStatistic(s3);
    	u2.addStatistic(s4);
    	u2.addStatistic(s5);
    	u3.addStatistic(s6);
    	u3.addStatistic(s7);
    	u3.addStatistic(s8);
    	u4.addStatistic(s9);
    	
    	userRepo.save(u1);
    	userRepo.save(u2);
    	userRepo.save(u3);
    	userRepo.save(u4);
    	userRepo.save(u5);
		
		return "stats/index";
	}
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public String getUserStats(final @PathVariable Long userId, Model model) {
		model.addAttribute("userStats", statService.getAllUserStats(userId));
		return "stats/index";
	}
	
	@RequestMapping(value = "/{userId}/{gymnasticId}", method = RequestMethod.GET)
	public String getUserStatsByGymnastic(final @PathVariable Long userId,
										  final @PathVariable Long gymnasticId,
										  Model model) {
		model.addAttribute("userStats", statService.getAllUserStatsByGymnastic(userId, gymnasticId));
		return "stats/index";
	}
}
