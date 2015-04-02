package ru.aspu.medstat.controllers;

import java.security.Principal;
import java.util.List;

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
import ru.aspu.medstat.repositories.GymnasticRepository;
import ru.aspu.medstat.repositories.StatisticsRepository;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.services.StatisticsService;

@Controller
@RequestMapping("/lk")
public class CabinetController {
	private static final Logger log = Logger.getLogger(CabinetController.class);
	
	@Autowired
	private StatisticsService statService;
	
	@Autowired
	private StatisticsRepository statRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private GymnasticRepository gymRepo;
	
	@RequestMapping("/")
    public String index(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		
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
		
		s1.percent = 100.0;
		s1.setUser(user);
		s1.setGymnastic(g1);
		
		s2.percent = 50.0;
		s2.setUser(user);
		s2.setGymnastic(g2);
		
		s3.percent = 0.0;
		s3.setUser(user);
		s3.setGymnastic(g3);
		
		statRepo.save(s1);
		statRepo.save(s2);
		statRepo.save(s3);
				
		if (user.role == User.Roles.PATIENT.getValue()) {
			return "redirect:user/";
		} else if (user.role == User.Roles.DOCTOR.getValue()) {
			return "redirect:doctor/";
		}
		return "index/index";
	}
	
	@RequestMapping("/user")
	public String userIndex(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		model.addAttribute("userStats", statService.getAllUserStats(user.id));
		
		return "cabinet/user/index";
	}
	
	@RequestMapping("/doctor")
	public String doctorIndex(Model model, Principal principal) {
		return "cabinet/doctor/index";
	}
	
	@RequestMapping(value = "/user/gym/{gymId}", method = RequestMethod.GET)
	public String getStatsByUserGym(final @PathVariable Long gymId,
									Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		List<Statistic> userStats = statService.getAllUserStatsByGymnastic(user.id, gymId);
		model.addAttribute("userStats", userStats);
		return "cabinet/user/index";
	}
	
	@RequestMapping(value = "/doctor/users/{userId}")
	public String getStatsByUser(final @PathVariable Long userId,
								 Model model) {
		User user = userRepo.findOne(userId);
		model.addAttribute("userStats", statService.getAllUserStats(user.id));
		return "cabinet/user/index";
	}
	
	@RequestMapping(value = "/doctor/users/")
	public String getStatsForAllUsers(Model model) {
		model.addAttribute("userStats", statService.getAllStats());
		return "cabinet/user/index";
	}
	
	@RequestMapping(value = "/doctor/users/{userId}/gym/{gymId}")
	public String getStatsByUserAndGym(final @PathVariable Long userId,
									   final @PathVariable Long gymId,
									   Model model) {
		model.addAttribute("userStats", statService.getAllUserStatsByGymnastic(userId, gymId));
		return "cabinet/user/index";
	}
}
