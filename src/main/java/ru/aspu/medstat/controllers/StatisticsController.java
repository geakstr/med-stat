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
//        User u1 = userRepo.findOne((long) 3);
//
//        Gymnastic g1 = new Gymnastic();
//        Gymnastic g2 = new Gymnastic();
//        Gymnastic g3 = new Gymnastic();
//
//        g1.title = "Gymnastic #1";
//        g2.title = "Gymnastic #2";
//        g3.title = "Gymnastic #3";
//
//        gymRepo.save(g1);
//        gymRepo.save(g2);
//        gymRepo.save(g3);
//
//        Statistic s1 = new Statistic();
//        Statistic s2 = new Statistic();
//        Statistic s3 = new Statistic();
//
//        s1.percent = 100.0;
//        s1.setGymnastic(g1);
//
//        s2.percent = 100.0;
//        s2.setGymnastic(g2);
//
//        s3.percent = 100.0;
//        s3.setGymnastic(g3);
//
//        u1.addStatistic(s1);
//        u1.addStatistic(s2);
//        u1.addStatistic(s3);
//
//        userRepo.save(u1);

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
