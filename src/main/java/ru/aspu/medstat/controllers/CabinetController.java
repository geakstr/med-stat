package ru.aspu.medstat.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.aspu.medstat.entities.Statistic;
import ru.aspu.medstat.entities.User;
import ru.aspu.medstat.forms.AdminDoctorRegistrationForm;
import ru.aspu.medstat.forms.AdminSetDoctorToPacientForm;
import ru.aspu.medstat.repositories.UserRepository;
import ru.aspu.medstat.responses.ErrorResponse;
import ru.aspu.medstat.responses.IResponse;
import ru.aspu.medstat.responses.UserResponse;
import ru.aspu.medstat.services.MailService;
import ru.aspu.medstat.services.StatisticsService;
import ru.aspu.medstat.services.UsersService;
import ru.aspu.medstat.utils.EmailUtils;
import ru.aspu.medstat.utils.PasswordUtils;

@Controller
@RequestMapping("/lk")
public class CabinetController {
	private static final Logger log = Logger.getLogger(CabinetController.class);
	
	@Autowired
	private StatisticsService statService;
	
	@Autowired
	private UserRepository userRepo;
	
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private MailService mail;
	
	@RequestMapping("/")
    public String index(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
			
		if (user.role == User.Roles.PATIENT.getValue()) {
			return "redirect:user/";
		} else if (user.role == User.Roles.DOCTOR.getValue()) {
			return "redirect:doctor/";
		} else if (user.role == User.Roles.ADMIN.getValue()) {
			return "redirect:admin/";
		}
		
		return "redirect:/";
	}
	
	@RequestMapping("/admin")
	public String adminIndex(Model model) {
		model.addAttribute("AdminDoctorRegistrationForm", new AdminDoctorRegistrationForm());
        model.addAttribute("AdminSetDoctorToPacientForm", new AdminSetDoctorToPacientForm());

        model.addAttribute("usersList", userRepo.findAllNewUsers());
        model.addAttribute("doctorsList", userRepo.findAllDoctors());
        
		return "cabinet/admin/index";
	}
	
	@RequestMapping("/user")
	public String userIndex(Model model, Principal principal) {
		User user = userRepo.findByEmail(principal.getName());
		model.addAttribute("userStats", statService.getAllUserStats(user.id));
		
		return "cabinet/user/index";
	}
	
	@RequestMapping("/doctor")
	public String doctorIndex(Model model, Principal principal) {
		User doctor = userRepo.findByEmail(principal.getName());
		model.addAttribute("doctorPacients", userRepo.findAllPacientByDoctor(doctor.id));
		return "cabinet/doctor/index";
	}
	
    @RequestMapping(value = "/admin/doctors", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public IResponse registerDoctor(@RequestBody AdminDoctorRegistrationForm form) {
        String error = "";
        if (null != userRepo.findByEmail(form.getEmail())) {
            error += "Такая эл. почта уже занята\n";
        }
        if (!EmailUtils.validate(form.getEmail())) {
            error += "Невалидный адрес эл. почты. Принимается паттерн вида *@*\n";
        }

        if (error.length() != 0) {
            return new ErrorResponse(error);
        }

        final User doctor = new User();

        doctor.email = form.getEmail();
        doctor.role = User.Roles.DOCTOR.getValue();
        doctor.wasLogin = true;
        doctor.emailToken = PasswordUtils.generate(32);

        usersService.sendMail(doctor);

        return new UserResponse(userRepo.save(doctor));
    }


    @RequestMapping(value = "/admin/requests", method = RequestMethod.POST)
    public String setDoctorToPacient(@ModelAttribute AdminSetDoctorToPacientForm form, 
    								 @ModelAttribute ArrayList<User> usersList, Model model) {
        final User user = userRepo.findOne(form.getPacient());
        user.doctorId = form.getDoctor();

        for (ListIterator<User> iter = usersList.listIterator(); iter.hasNext(); ) {
            User someUser = iter.next();
            if (user.equals(someUser)) {
                iter.remove();
            }
        }

        return "redirect:/";
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
	public String getStatsForAllUsers(Model model, Principal principal) {
		User doctor = userRepo.findByEmail(principal.getName());
		model.addAttribute("userStats", statService.getAllUsersStatsByDoctor(doctor.id));
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